package com.association.configsend.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "role")
@Data
public class RoleConfiguration {
    private String common = "UNKNOWN";
}
