package com.association.user.keeper;

import com.alibaba.fastjson.JSONObject;
import com.association.common.all.util.log.ILogger;
import com.association.user.component.RedisKeyEnum;
import com.association.user.component.RedisUtil;
import com.association.user.component.TokenLifeConfiguration;
import com.association.user.mapper.GroupMapper;
import com.association.user.mapper.UserMapper;
import com.association.user.condition.ConditionForUser;
import com.association.user.model.UserDO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class UserKeeper {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    ILogger logger;
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
        if(StringUtils.isEmpty(userDO.getName())){
            userDO.setName(new StringBuilder("游客").append(":").append(UUID.randomUUID().toString().replaceAll("-","").substring(0,6).toString()).toString());
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
        redisUtil.set(key,value,getTokenLife());
        return Boolean.TRUE;
    }

    //unit second
    public Long getTokenLife(){
        Long life = tokenLifeConfiguration.getLife();
        TimeUnit unit = tokenLifeConfiguration.getUnit();
        return unit.toSeconds(life);
    }
    public Boolean checkToken(String key){
       Object res =  redisUtil.get(key);
       logger.info("USERTOKEN : {}",JSONObject.toJSONString(res));
       return res != null ;
    }

    public List<String> getUserGuidByGroupGuid(String guid){
        return groupMapper.getUserGuidsByGroupGuid(guid);
    }

    public List<UserDO> getUsers(ConditionForUser condition){
        return userMapper.getUsers(condition);
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
