package com.association.workflow.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
@AllArgsConstructor
@NoArgsConstructor
public class RedisLock {

    RedisTemplate redisTemplate;
    String key;
    String randomValue = UUID.randomUUID().toString().replace("-","");
    Integer timeout;
//    TimeUnit timeUnit ;

    public RedisLock(RedisTemplate redisTemplate, String key, Integer time) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.timeout = time;
    }

    public Boolean lock(){
        Object res = redisTemplate.execute(RedisScript.of(luaScript()), null);
        return res == null ? Boolean.FALSE : ((Long)res == 1) ;
    }

    public void unLock(){
        if(String.valueOf(redisTemplate.opsForValue().get(key)).equals(randomValue)) {
            redisTemplate.delete(key);
        }
    }
    private String luaScript(){
        return "local key = '" + key + "'; "
                + "local ok = redis.call('setnx',key,1);"
                + "if ok == 1 then redis.call('expire', key, " + timeout + "); end;"
                + "return ok;";
    }
}
