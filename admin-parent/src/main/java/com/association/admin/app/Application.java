package com.association.admin.app;

import com.association.common.all.util.log.ILogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.association.*","com.association.admin.app"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.association.*"})
public class Application {
    public static ConfigurableApplicationContext ctx;
    private static ILogger logger = new ILogger();

    public static void main(String[] args) {
        logger.info("Application starting...");
        ctx = SpringApplication.run(Application.class,args);
        logger.info("Application started...");

    }
}
