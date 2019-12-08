package com.association.configsend.app;

import com.association.common.all.util.log.ILogger;
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

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.association.configsend" , "com.association.*"})
public class Application {
    public static ILogger logger = ILogger.getInstance();
    public static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        logger.info("application starting...");
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        Application.ctx = ctx;
        logger.info("application started...");

    }


}
