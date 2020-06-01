package com.association.common.all.util.log;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class LoggerConfiguration {
@Value("${spring.application.name}")
    private String appName = "UNKNOWN" ;
}
