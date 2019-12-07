package com.association.common.all.util.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class TestConfig {
    public int i;

    @Bean
    @Primary
    public TestConfig testConfig() {
        TestConfig config = new TestConfig();
        config.i = 1;
        return config;
    }
}
