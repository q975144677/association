package com.association.user.app;

import com.alibaba.fastjson.JSONObject;
import com.association.common.all.util.log.ILogger;
import com.association.user.component.RedisUtil;
import com.association.user.component.TokenLifeConfiguration;
import com.association.user.keeper.UserKeeper;
import com.association.user.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.logging.Logger;
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.association")
@ComponentScan(basePackages = {"com.association.user","com.associationNew"})
@SpringBootApplication
@MapperScan(basePackages = {"com.association.user.mapper"})
public class Application {


    public static ConfigurableApplicationContext ctx;

    private static ILogger logger = new ILogger();
    public static void main(String[] args) {
        logger.info("Application starting...");
        ctx = SpringApplication.run(Application.class, args);
        logger.info("Application started...");
//        System.out.println(ctx.getBean(UserKeeper.class) == ctx.getBean(UserKeeper.class));
//        System.out.println(ctx.getBean(RedisUtil.class) == ctx.getBean(RedisUtil.class));
//        System.out.println(ctx.getBean(UserServiceImpl.class) == ctx.getBean(UserServiceImpl.class));
    }
}
