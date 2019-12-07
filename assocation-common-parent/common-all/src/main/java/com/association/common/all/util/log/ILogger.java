package com.association.common.all.util.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class ILogger {
    public static ILogger getInstance(){
        return new ILogger();
    }
    private Logger info = LoggerFactory.getLogger("info");
    private Logger error = LoggerFactory.getLogger("error");
    private Logger debug = LoggerFactory.getLogger("debug");
    private Logger common = LoggerFactory.getLogger("common");
    private Logger warn = LoggerFactory.getLogger("warn");

    {
        if (info == null) {
            info = LoggerFactory.getLogger(this.getClass());
        }
        if (error == null) {
            error = LoggerFactory.getLogger(this.getClass());
        }
        if (debug == null) {
            debug = LoggerFactory.getLogger(this.getClass());
        }
        if (common == null) {
            common = LoggerFactory.getLogger(this.getClass());
        }
        if (warn == null) {
            warn = LoggerFactory.getLogger(this.getClass());
        }

    }
    public void info(Object obj){
        try {
            common.info(new ObjectMapper().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            error("logger error :{}" , obj);
        }
    }
    public void error(Object obj){
        try {
            error.error(new ObjectMapper().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            error("logger error :{}" , obj);
        }
    }
    public void debug(Object obj){
        try {
            debug.debug(new ObjectMapper().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            error("logger error :{}" , obj);
        }
    }
    public void warn(Object obj){
        try {
            warn.warn(new ObjectMapper().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            error("logger error :{}" , obj);
        }
    }

    public void service(String content, Object... obj) {
        info.info(content, obj);
    }
    public void warn(String content , Object... obj){
        warn.warn(content,obj);
    }
    public void error(String content ,Object... obj){
        error.error(content,obj);
    }
    public void debug(String content,Object... obj){
        debug.debug(content,obj);
    }
    public void info(String content, Object... obj){
        common.info(content,obj);
    }
    public void service(String content){
        info.info(content);
    }
    public void error(String content){
        error.error(content);
    }
    public void debug(String content){
        debug.debug(content);
    }
    public void info(String content){
        common.info(content);
    }
    public void warn(String content) {
        warn.warn(content);
    }
}
