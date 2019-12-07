package com.association.user.app;

import com.association.common.all.util.log.ILogger;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.logging.Logger;
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.association.user"})
@SpringBootApplication
public class Application {

    public static ConfigurableApplicationContext ctx;

    private static ILogger logger = new ILogger();
    public static void main(String[] args) {
        logger.info("Application starting...");
        ctx = SpringApplication.run(Application.class, args);
        logger.info("Application started...");
    }
}
