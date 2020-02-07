package com.association.workflow.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients("com.association")
@EnableDiscoveryClient
@MapperScan("com.association.workflow")
@ComponentScan(basePackages = {"com.association.workflow"})
public class Application {
public static void main(String[] args){
    SpringApplication.run(Application.class,args);
}
}
