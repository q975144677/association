package com.association.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.association.admin.component.RedisUtil;
import com.association.common.all.util.log.ILogger;
import com.association.common.all.util.log.OSSHelper;
import com.association.common.all.util.log.SMSHelper;
import com.association.config.iface.ConfigIface;
import com.association.config.model.SchoolDTO;
import com.association.user.Iface.UserIface;
import com.association.user.condition.ConditionForUser;
import com.association.user.model.UserDO;
import com.association.user.model.UserSM;
import com.association.workflow.model.SchoolDO;
import component.BasicComponent;
import component.HttpRequestException;
import component.Proto;
import component.TokenCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("user")
public class UserController extends BasicComponent {
    @Autowired
    UserIface userIface;
    @Autowired
    RedisUtil redis;
    @Autowired
    ConfigIface configIface;
    @Autowired
    ThreadPoolExecutor defaultThreadPool;
@Autowired
    ILogger logger;
    @TokenCheck(false)
    @PostMapping("login")
    public Proto<?> login(@RequestBody ConditionForUser condition) {
        return getResult(userIface.login(condition));
    }

    @TokenCheck(false)
    @PostMapping("register")
    public Proto<?> register(@RequestBody UserDO userDO) {
        return getResult(userIface.register(userDO));
    }

    @TokenCheck(false)
    @PostMapping("getLoginInfo")
    public Proto<?> getLoginInfo(@RequestBody String token) {
        return getResult(userIface.getUserByToken(token));
    }

    //手机登录用
    @TokenCheck(false)
    @PostMapping("loginByMobile")
    public Proto<String> checkMobileCode(@RequestBody Map<String, String> param, HttpServletRequest request) {
        String reqCode = param.get("reqCode");
        String userName = param.get("mobilePhone");
        String code = (String) redis.get(String.join(":", MainController.RedisKey.DEFAULT_CODE.name(), userName));
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(reqCode) || !reqCode.equals(code)) {
            return fail("验证码错误");
        }
        redis.del(String.join(":", MainController.RedisKey.DEFAULT_CODE.name(), userName));
        ConditionForUser condition = new ConditionForUser();
        Proto<UserDO> loginUserResult = userIface.getUser(condition);
        String token = "登录失败";
        if (loginUserResult != null && loginUserResult.getData() != null) {
            token = userIface.setToken(loginUserResult.getData()).getData();
        } else {
            return fail("登录失败");
        }
        return getResult(token);
    }

    @TokenCheck(false)
    @PostMapping("sendCode")
    public Proto<?> sendCode(@RequestBody Map<String, String> param, HttpServletRequest request) {
        String mobile = param.get("mobile");
        if (StringUtils.isEmpty(mobile)) {
            throw HttpRequestException.defaultParamErrorException();
        }
        String code = getRandomCode(4);
        // 15 分钟
        redis.set(String.join(":", MainController.RedisKey.DEFAULT_CODE.name(), mobile), code, 15 * 60);
        //todo 短信发送
        SMSHelper.sendCodeAsync(code, mobile);
        //done
        return getResult(true);
    }

    //手机注册用
    @TokenCheck(false)
    @PostMapping("registerByMobile")
    public Proto<?> registerByMobile(@RequestBody Map<String, String> param, HttpServletRequest request) {
        String reqCode = param.get("reqCode");
        String mobile = param.get("mobile");
        String password = param.get("password");
        String name = param.get("name");
        if (password == null || password.length() < 6 || StringUtils.isEmpty(mobile)) {
            return fail(HttpRequestException.PARAM_ERROR);
        }
        //注册不发送验证码了
//        String code = (String) redis.get(String.join(":", MainController.RedisKey.DEFAULT_CODE.name(), mobile));
//        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(reqCode) || !reqCode.equals(code)) {
//            return getResult(Boolean.FALSE);
//        }
//        redis.del(String.join(":", MainController.RedisKey.DEFAULT_CODE.name(), mobile));
//        //todo 注册用户
//        String guid;
//        String token = UUID.randomUUID().toString().replaceAll("-", "");
//        //redis.set(String.join(":",RedisKey.TOKEN.name(),token),guid);
        UserDO userDO = new UserDO();
        userDO.setMobilePhone(mobile);
        userDO.setUsername(mobile);
        userDO.setPassword(password);
        userDO.setName(name);
        return userIface.register(userDO);
    }

    @TokenCheck(false)
    @RequestMapping("schools")
    public Proto<?> getSchools() {
        List<JSONObject> result = new ArrayList<>();
        configIface.schools().getData().getAreas().stream().map(area -> area.getSchools()).forEach(schools -> {
            schools.stream().forEach(school -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", school.getCode());
                jsonObject.put("name", school.getName());
                result.add(jsonObject);
            });
        });
        return getResult(result);
    }

    @TokenCheck(false)
    @PostMapping("checkToken")
    public Proto<Boolean> checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return getResult(Boolean.FALSE);
        }
        return getResult(userIface.checkToken(token));
    }

    @PostMapping("getUser")
    public Proto<Operator> getUser(HttpServletRequest request) {
        Operator operator = getOperator(request);
        System.out.println(JSONObject.toJSONString(request.getAttribute("user")));
        System.out.println(JSONObject.toJSONString(request.getAttribute("operator")));
        if (operator == null) {
            throw new HttpRequestException(HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED.value());
        }
        Integer schoolId = operator.getSchoolId();
        Proto<SchoolDTO> proto = configIface.schools();
        if (proto != null && proto.getData() != null) {
            SchoolDTO schoolDTO = proto.getData();
            Stream<SchoolDTO.Area> areaStream = schoolDTO.getAreas().stream();
            Map<Integer, String> schoolCodeNameMap = areaStream.flatMap(x -> x.getSchools().stream()).collect(Collectors.toMap(SchoolDTO.School::getCode, SchoolDTO.School::getName));
            operator.setSchoolName(schoolCodeNameMap.get(schoolId));
        }
        return getResult(operator);
    }


    @PostMapping("addAvatar")
    public Proto<Boolean> addAvatar(MultipartFile file, HttpServletRequest request) {
//        logger.info("File:{}",file.getName());
  //      logger.info("FILE IN FILE IN FILE : {} " , file.getSize());
    String filename = UUID.randomUUID().toString().replaceAll("-","")+".jpg";
        if (!OSSHelper.pushFile(file,filename)) {
            return fail("上传文件失败");
        }
        String url = OSSHelper.buildUrl(filename);
        Operator operator = getOperator(request);
        UserDO userDO = new UserDO();
        userDO.setGuid(operator.getGuid());
        userDO.setAvatar(url);
        return userIface.updateUser(userDO);
    }

    @PostMapping("update")
    public Proto<Boolean> update(@RequestBody UserSM userDO, HttpServletRequest request) {
        Operator operator = getOperator(request);
        boolean token = true ;
        userDO.setGuid(operator.getGuid());
        if(!StringUtils.isEmpty(userDO.getPassword())) {
            String by = operator.getPassword();
            token  = BCrypt.checkpw(userDO.getOldPassword(), by);
        }
        if (token) {
            userDO.setUsername(null);
            return userIface.updateUser(userDO);
        }
        return getResult(Boolean.FALSE);
    }

    @PostMapping("resendCodeForPassword")
    public Proto<Boolean> resendCodeForPassword(HttpServletRequest request) {
        Operator operator = getOperator(request);
        String mobilePhone = operator.getMobilePhone();
        String code = getRandomCode(4);
        redis.set(MainController.RedisKey.PASSWORD_CODE + mobilePhone, code, 15 * 60);
        SMSHelper.sendCodeAsync(code, mobilePhone);
        return getResult(Boolean.TRUE);
    }

    @PostMapping("checkPasswordCode")
    public Proto<Boolean> checkPasswordCode(@RequestBody Map<String, String> codeMap, HttpServletRequest request) {
        String code = codeMap.get("code");
        Operator operator = getOperator(request);
        String mobilePhone = operator.getMobilePhone();
        if (StringUtils.isEmpty(code)) {
            return getResult(Boolean.FALSE);
        }
        String value = String.valueOf(redis.get(MainController.RedisKey.PASSWORD_CODE+mobilePhone));
        logger.info(value + " - " + code);
        return getResult(code.equals(value));
    }

    private String getRandomCode(int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int temp = (int) (Math.random() * 10);
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

}
