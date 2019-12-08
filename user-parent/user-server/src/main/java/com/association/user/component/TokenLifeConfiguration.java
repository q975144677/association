package com.association.user.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "user.token")
@Data
public class TokenLifeConfiguration {
    private Long life ;
    private TimeUnit unit;
}
