package com.association.user.service;

import com.association.user.component.RedisKeyEnum;
import com.association.user.component.RedisLock;
import com.association.user.component.RoleConfiguration;
import com.association.user.keeper.UserKeeper;
import com.associtaion.user.Iface.RoleIface;
import com.associtaion.user.Iface.UserIface;
import com.associtaion.user.condition.ConditionForUser;
import com.associtaion.user.model.UserDO;
import component.BasicService;
import component.Proto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserServiceImpl extends BasicService implements UserIface {
    @Autowired
    UserKeeper userKeeper;
    @Autowired
    RoleConfiguration roleConfiguration;
    @Autowired
    RedisTemplate redisTemplate ;
    @Override
    public Proto<String> register(UserDO condition) {
        if (StringUtils.isEmpty(condition.getUsername()) || StringUtils.isEmpty(condition.getPassword())) {
            return Proto.fail("参数错误");
        }
        if (StringUtils.isEmpty(condition.getRoleGuid())) {
            String roleGuid = roleConfiguration.getCommon();
            condition.setRoleGuid(roleGuid);
        }
        ConditionForUser conditionForUser = new ConditionForUser();
        conditionForUser.setUsername(condition.getUsername());
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
            if(userDO != null){
                result = "呜呜呜，账户好像被注册了";
                break Label;
            }
            Boolean create = userKeeper.register(condition);
            if (create) {
                String key = String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token);
                Boolean addToken = userKeeper.addRedisString(key, condition);
                status = 1 ;
            }
        }
        lock.unLock();
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
        Boolean checkPassword = BCrypt.checkpw(condition.getPassword(), userDO.getPassword());
        if (!checkPassword) {
            return fail();
        }
        // token - user 存放入 redis
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String key = String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token);
        Boolean res = userKeeper.addRedisString(key, userDO);
        if (!res) {
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

    public static void main(String[] args) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token));
    }
}
