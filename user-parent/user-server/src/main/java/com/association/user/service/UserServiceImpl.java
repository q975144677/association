package com.association.user.service;

import com.alibaba.fastjson.JSONObject;
import com.association.common.all.util.log.ILogger;
import com.association.user.component.RedisKeyEnum;
import com.association.user.component.RedisLock;
import com.association.user.component.RoleConfiguration;
import com.association.user.keeper.UserKeeper;
import com.association.user.Iface.UserIface;
import com.association.user.condition.ConditionForUser;
import com.association.user.model.UserDO;
import component.BasicService;
import component.Proto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
//todo 使用手机号注册时 username 默认手机号
public class UserServiceImpl extends BasicService implements UserIface {
    @Autowired
    UserKeeper userKeeper;
    @Autowired
    RoleConfiguration roleConfiguration;
    @Autowired
    RedisTemplate redisTemplate ;
    @Autowired
    ILogger logger;
    @Override
    public Proto<String> register(UserDO condition) {
        if (StringUtils.isEmpty(condition.getPassword()) || (StringUtils.isEmpty(condition.getUsername()) && StringUtils.isEmpty(condition.getMobilePhone()))) {
            return Proto.fail("参数错误");
        }
        if(StringUtils.isEmpty(condition.getUsername()) && !StringUtils.isEmpty(condition.getMobilePhone())){
            condition.setUsername(condition.getUsername());
        }
        if (StringUtils.isEmpty(condition.getRoleGuid())) {
            String roleGuid = roleConfiguration.getCommon();
            condition.setRoleGuid(roleGuid);
        }
        if(StringUtils.isEmpty(condition.getName())){
            StringBuilder stringBuilder = new StringBuilder().append("游客").append("-");
            for(int i = 0 ; i < 6 ; i ++ ){
                stringBuilder.append(new Random().nextInt(10));
            }
            condition.setName(stringBuilder.toString());
        }
        ConditionForUser conditionForUser = new ConditionForUser();
        conditionForUser.setUsername(condition.getUsername());
        logger.info("lock the user : {}",conditionForUser);
        RedisLock lock = new RedisLock(redisTemplate,String.format(RedisKeyEnum.REDIS_LOCK_FOR_REGISTER.getPattern(),condition.getUsername()),5);
        Boolean success = lock.lock();
        String result = null ;
        int status = 0 ;
        if(!success){
            result = "被人抢先一步了";
            return Proto.fail(result);
        }
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Label : {
            UserDO userDO = userKeeper.getUser(conditionForUser);
            logger.info("userDO : {}" , userDO);
            if(userDO != null){
                result = "呜呜呜，账户好像被注册了";
                break Label;
            }
            ConditionForUser conditionForUser1 = new ConditionForUser();
            conditionForUser1.setMobilePhone(condition.getMobilePhone());
            UserDO userDo2 = userKeeper.getUser(conditionForUser1);
            logger.info("USERDO FOR MOBILE {}", JSONObject.toJSONString(userDo2));
            if(userDo2 != null){
                result =  "呜呜呜，手机号好像被注册了";
                break Label;
            }
            Boolean create = userKeeper.register(condition);
            logger.info("register{}", create);
            if (create) {
                String key = String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token);
                UserDO user = userKeeper.getUser(conditionForUser);
                Boolean addToken = userKeeper.addRedisString(key, user);
                status = 200 ;
            }
        }
        lock.unLock();
        logger.info("unlock ");
        if(status == 0){
            return Proto.fail(result);
        }
        return getResult(token);
    }

    @Override
    public Proto<String> login(ConditionForUser condition) {
        if (StringUtils.isEmpty(condition.getPassword()) || StringUtils.isEmpty(condition.getUsername())) {
            return fail();
        }
        UserDO userDO = userKeeper.getUser(condition);
        if(userDO == null){
            logger.error("user is not exists");
            return fail();
        }
        Boolean checkPassword = BCrypt.checkpw(condition.getPassword(), userDO.getPassword());
        if (!checkPassword) {
            logger.error("密码错误");
            return fail();
        }
        // token - user 存放入 redis
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String key = String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token);
        Boolean res = userKeeper.addRedisString(key, userDO);
        if (!res) {
            logger.error("REDIS 错误");
            return fail();
        }
        return getResult(token);
    }

    @Override
    public Proto<UserDO> getUserByToken(String token) {
        return getResult(userKeeper.getUserByToken(token));
    }

    @Override
    public Proto<Boolean> updateUser(UserDO user) {
        return getResult(userKeeper.updateUser(user));
    }

    @Override
    public Proto<List<UserDO>> getUsers(ConditionForUser condition) {
        return getResult(userKeeper.getUsers(condition));
     }

    @Override
    public Proto<String> setToken(UserDO userDO) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String key = String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token);
        Boolean res = userKeeper.addRedisString(key, userDO);
        return getResult(token);
    }



    @Override
    public Proto<UserDO> getUser(ConditionForUser condition) {
        return getResult(userKeeper.getUser(condition));
    }

    @Override
    public Proto<Boolean> checkToken(String token) {
        String key = String.format(RedisKeyEnum.USER_TOKEN.getPattern(),token);
        return getResult(userKeeper.checkToken(key));
    }

    @Override
    public Proto<Boolean> refrushToken(String token) {
        UserDO userDO = userKeeper.getUserByToken(token);
        if(userDO == null){
            return getResult(Boolean.FALSE);
        }
        String guid = userDO.getGuid();
        ConditionForUser conditionForUser = new ConditionForUser();
        conditionForUser.setGuid(guid);
        UserDO userNew = userKeeper.getUser(conditionForUser);
        if(userNew == null){
            return getResult(Boolean.FALSE);
        }
        // refrush redis ;
        String key = String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token);
        return getResult(userKeeper.addRedisString(key,userNew));
    }

    public static void main(String[] args) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token));
    }
}
