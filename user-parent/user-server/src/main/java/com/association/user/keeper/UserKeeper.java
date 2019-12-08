package com.association.user.keeper;

import com.alibaba.fastjson.JSONObject;
import com.association.user.component.RedisKeyEnum;
import com.association.user.component.RedisLock;
import com.association.user.component.RedisUtil;
import com.association.user.component.TokenLifeConfiguration;
import com.association.user.mapper.UserMapper;
import com.associtaion.user.Iface.UserIface;
import com.associtaion.user.condition.ConditionForUser;
import com.associtaion.user.model.UserDO;
import io.lettuce.core.Limit;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sun.security.util.Password;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UserKeeper {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    TokenLifeConfiguration tokenLifeConfiguration;
    public List<UserDO> queryUsers(ConditionForUser condition) {
        Integer pageIndex = condition.getPageIndex();
        Integer pageSize = condition.getPageSize();
        if (pageIndex == null || pageSize == null || pageIndex < 1) {
            pageIndex = 1;
            pageSize = 10;
        }
        int offset = (pageIndex - 1) * pageSize;
        Long recordCount = userMapper.countUsers(condition);
        Integer pageCount = Math.max(1, (int) Math.ceil((double) recordCount / (double) pageSize));
        condition.setOffset(offset);
        condition.setPageCount(pageCount);
        condition.setRecordCount(recordCount);
        return userMapper.queryUsers(condition, pageSize, offset);
    }

    public Boolean register(UserDO userDO) {
        if(StringUtils.isEmpty(userDO.getPassword())){
           throw new RuntimeException("password do not exist");
        }
        userDO.setPassword(BCrypt.hashpw(userDO.getPassword(),BCrypt.gensalt()));
        Boolean res = userMapper.createNewUser(userDO);
        return res;
    }

    public Boolean updateUser(UserDO userDO) {
        if(!StringUtils.isEmpty(userDO.getPassword())){
            userDO.setPassword(BCrypt.hashpw(userDO.getPassword(),BCrypt.gensalt()));
        }
        return userMapper.updateUser(userDO);
    }

    public UserDO getUserByToken(String token) {
        Object res = redisUtil.get(String.format(RedisKeyEnum.USER_TOKEN.getPattern(), token));
        //刷新 token 生存周期
        Long life = tokenLifeConfiguration.getLife();
        TimeUnit unit = tokenLifeConfiguration.getUnit();
        long convertLife = unit.toSeconds(life);
        redisUtil.expire(String.format(RedisKeyEnum.USER_TOKEN.getPattern(),token), convertLife);
        UserDO result =JSONObject.parseObject(JSONObject.toJSONString(res),UserDO.class);
        return result;
    }
    public UserDO getUser(ConditionForUser condition){
        return userMapper.getUser(condition);
    }

    public Boolean addRedisString(String key , Object value){
        Boolean set = redisUtil.set(key, value);
        if(set){
            Boolean exp = redisUtil.expire(key,getTokenLife());
            if(exp){
               return Boolean.TRUE;
            }
            redisUtil.del(key);
        }
        return Boolean.FALSE;
    }

    //unit second
    public Long getTokenLife(){
        Long life = tokenLifeConfiguration.getLife();
        TimeUnit unit = tokenLifeConfiguration.getUnit();
        return unit.toSeconds(life);
    }
    public static void main(String[] args) {
        assert 1 == 2;
        String s = BCrypt.hashpw("123",BCrypt.gensalt());
        System.out.println(s);
        System.out.println(BCrypt.checkpw("123",s));
        System.out.println(2);
        assert 1 == 1;
        System.out.println(1);
         Assert.hasText("1ds", "ds");
        long onefourdays = TimeUnit.DAYS.toSeconds(14);
        System.out.println(onefourdays);
    }

}
