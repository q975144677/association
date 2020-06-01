package com.association.admin.component;

import com.alibaba.fastjson.JSONObject;
import com.association.common.all.util.log.ILogger;
import com.association.config.iface.ConfigIface;
import com.association.config.model.SchoolDTO;
import com.association.user.Iface.RoleIface;
import com.association.user.Iface.UserIface;
import com.association.user.condition.ConditionForUser;
import com.association.user.model.UserDO;
import component.BasicComponent;
import component.HttpRequestException;
import component.Proto;
import component.TokenCheck;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Operator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuthInterceptorNew implements HandlerInterceptor {
    @Autowired
    UserIface userIface;
    @Autowired
    ILogger logger;
    @Autowired
    RoleIface roleIface;
    @Autowired
    ConfigIface configIface;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof  HandlerMethod)){
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        System.out.println("--------");
        System.out.println(method.getName());
        System.out.println(request.getRequestURI());
        System.out.println("--------");
        String token = request.getHeader("token");
        TokenCheck annotation = method.getAnnotation(TokenCheck.class);
        if (annotation != null && !annotation.value()) {
            return true;
        }
        if(StringUtils.isEmpty(token)){
            throw new HttpRequestException("请登录",401);
        }
        Proto<UserDO> userResult = userIface.getUserByToken(token);
        System.out.println(token + "REDIS : {} " + JSONObject.toJSONString(userResult));
        if(userResult != null && userResult.getData() != null){
            UserDO user=  userResult.getData();
            System.out.println(JSONObject.toJSONString(user));
            BasicComponent.Operator operator = new BasicComponent.Operator();
            try {
                Proto<SchoolDTO> proto = configIface.schools();
                if (proto != null && proto.getData() != null) {
                    SchoolDTO schoolDTO = proto.getData();
                    Stream<SchoolDTO.Area> areaStream = schoolDTO.getAreas().stream();
                    Map<Integer, String> schoolCodeNameMap = areaStream.flatMap(x -> x.getSchools().stream()).collect(Collectors.toMap(SchoolDTO.School::getCode, SchoolDTO.School::getName));
                    operator.setSchoolName(schoolCodeNameMap.get(user.getSchoolId()));
                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }
            request.setAttribute("operator",JSONObject.parseObject(JSONObject.toJSONString(user), BasicComponent.Operator.class));
            request.setAttribute("user",user);
        }else{
            throw new HttpRequestException("401", HttpStatus.UNAUTHORIZED.value());
        }
        return Boolean.TRUE;
    }

    @Override
    //refrush redis token
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return ;
        }
        userIface.refrushToken(token);
    }
}
