package com.association.admin.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterCeptorConfig implements WebMvcConfigurer {
//    @Autowired
//    AuthInterceptorNew interceptorNew;
@Bean
public AuthInterceptorNew interceptorNew(){
    return new AuthInterceptorNew();
}
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(interceptorNew());
        registration.addPathPatterns("/**");
    }
}
