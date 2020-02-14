package com.association.configsend.app;

import com.alibaba.fastjson.JSONObject;
import com.association.common.all.util.log.ILogger;
import com.association.config.model.MessageDTO;
import com.association.configsend.component.RedisUtil;
//import com.association.configsend.service.WebsocketEndPoint;
import com.association.configsend.util.ConfigUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.association")
@ComponentScan(basePackages = {"com.association.configsend" , "com.association.*" , "com.associationNew"})
public class Application {
    public static ILogger logger = ILogger.getInstance();
    public static ConfigurableApplicationContext ctx;

    public static boolean flag = false ;
    public static void main(String[] args) {
        logger.info("application starting...");
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        Application.ctx = ctx;
        logger.info("application started...");
         String messageKey = "MESSAGE:%s";
        String key = String.format(messageKey, "d72886a83d2611ea85e78c04ba0c3a6d");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo("12625baa426e4843ad16699d7e4cc2ef");
        messageDTO.setMsgType(1);
        messageDTO.setContent("test");
        messageDTO.setFrom("1");
        messageDTO.setGuid("1");
        messageDTO.setToGuids(Arrays.asList(new String[]{"1"}));
        ctx.getBean(RedisUtil.class).sadd(key, JSONObject.toJSONString(messageDTO));
        System.out.println("#%^&*$%");
        System.out.println(JSONObject.toJSONString(ctx.getBean(RedisUtil.class).sGet(key)));
//        if(false) {
//            for (; ; ) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println("123");
//                if (!flag) {
//                    continue;
//                }
//                System.out.println("send");
//                ctx.getBean(WebsocketEndPoint.class).sendMessage("123", "d72886a83d2611ea85e78c04ba0c3a6d");
//                if (flag) {
//                    break;
//                }
//            }
//        }
    }


}
