package com.association.common.all.util.log;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.weaver.Advice;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 定义切面注解 并 纳入springboot 管理进行自动配置
@Aspect
@Component
public class LogAspect {
    // 自己的日志类
    ILogger logger = new ILogger();
@Autowired
LoggerConfiguration loggerConfiguration;
    //切面定义
    @Pointcut("(within(com.association..*)) && !(within(com.association.common.all.util.log.*)) ")
    public void advice() {
    }

    @Around("advice()")
    // 返回值即为对应的方法返回值 ，Object 代表 不改变返回值类型（反正多态可以强转回来）
    public Object cut(ProceedingJoinPoint pjp) throws Throwable {
        if (pjp.getTarget() instanceof ILogger || pjp.getTarget() instanceof Logger) {
            logger.info("aop error \n maybe cut the logger class ?");
        }
        Throwable trw = null;
        try {
            logger.info("appName : {} ; method : {} ; requestJson : {}", loggerConfiguration.getAppName() , pjp.getSignature().getName() , JSONObject.toJSONString(pjp.getArgs()));
        } catch (Exception e) {
            //do nothing
            logger.error("maybe method is null ?");
        }
        try {
            return pjp.proceed(pjp.getArgs());
        } catch (Throwable throwable) {
            trw = throwable;
            logger.error("maybe run method error : \n{}", throwable.getMessage());
        }
        throw trw;
    }

    // Object 型的result 代表对返回值无要求
    // Object result 一定要是第二个参数 不然报错
    @AfterReturning(pointcut = "advice()", returning = "result")
    public void afr(JoinPoint jp, Object result) {
        logger.info("method : {} end...", jp.getSignature().getName());
        if (jp.getTarget() instanceof ILogger || jp.getTarget() instanceof Logger) {
            logger.info("aop error \n maybe cut the logger class ?");
        }
        try {
            logger.info("appName : {} ; method : {} ; responseJson : {}", loggerConfiguration.getAppName() , jp.getSignature().getName() , JSONObject.toJSONString(jp.getArgs()));
        } catch (Exception e) {
            logger.error("parse result error : {}", e.getMessage());
        }
    }
}
