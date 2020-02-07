package com.association.user.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "role")
@Data
public class RoleConfiguration {
    private String common = "1234";
    private String teacher = "3234" ;
    private String admin = "4234";
}
