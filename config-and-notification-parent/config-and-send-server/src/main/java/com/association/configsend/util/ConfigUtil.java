package com.association.configsend.util;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import sun.security.krb5.Config;

import java.util.List;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "nacos.config")
@Data
public class ConfigUtil {
@Value("${spring.cloud.nacos.config.server-addr}")
private String serverAddress;


@Deprecated
private String test ;
@Deprecated
private String botest;

List<Prop> props ;

private ConfigService configService ;

    @Bean("defaultConfigUtil")
    @Primary
    public ConfigUtil configUtil(ConfigUtil configUtil){
        System.out.println(configUtil);;
        try {
            ConfigService configService = NacosFactory.createConfigService(serverAddress);
            this.configService = configService;
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return configUtil;
    }

    @Data
    static class Prop{
        String group ;
        String dataId ;
    }
}
