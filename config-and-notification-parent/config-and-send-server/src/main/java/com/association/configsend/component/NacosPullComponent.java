package com.association.configsend.component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.association.common.all.util.log.ILogger;
import com.association.common.all.util.log.TestConfig;
import com.association.config.model.AuthDTO;
import com.association.config.model.CategoryDTO;
import com.association.config.model.SchoolDTO;
import com.association.configsend.util.ConfigUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.lang.management.ManagementPermission;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class NacosPullComponent implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    ILogger logger;
    @Autowired
    ConfigUtil configUtil;
    public Yaml yaml = new Yaml();
    public static final String GROUP = "association";
    public static Executor executor = Executors.newCachedThreadPool();
    public ObjectMapper mapper = new ObjectMapper() ;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        logger.info("loading nacos...");
        loadCategory();
        loadAuth();
        loadScholl();
        logger.info("load end...");
    }

    private void loadCategory() {
        logger.info("loading category");
        try {
            CategoryDTO category = load(GROUP, "category", CategoryDTO.class, new Listener() {
                @Override
                public Executor getExecutor() {
                    return executor;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    logger.info("nacos renew : \n{} - {}", "category", s);
                    CategoryDTO.setInstance(yaml.loadAs(s, CategoryDTO.class));
                }
            });
            CategoryDTO.setInstance(category);
            logger.info("load end : {}", category);
        } catch (Exception e) {
            logger.error("category load error e = {}", e.getMessage());
        }
    }

    private void loadScholl() {
        logger.info("loading school");
        try {
            SchoolDTO school = load(GROUP, "school", SchoolDTO.class, new Listener() {
                @Override
                public Executor getExecutor() {
                    return executor;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    logger.info("nacos renew : \n{} - {}", "school", s);
                    SchoolDTO.setInstance(yaml.loadAs(s, SchoolDTO.class));
                }
            });
            SchoolDTO.setInstance(school);
            logger.info("load end : {}", school);
        } catch (Exception e) {
            logger.error("school load error e = {}", e.getMessage());
        }
    }

    private void loadAuth() {
        logger.info("loading auth");
        try {
            AuthDTO auth = load(GROUP, "auth", AuthDTO.class, new Listener() {
                @Override
                public Executor getExecutor() {
                    return executor;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    logger.info("nacos renew : \n{} - {}", "auth", s);
                    AuthDTO.setInstance(yaml.loadAs(s, AuthDTO.class));
                }
            });
            AuthDTO.setInstance(auth);
            logger.info("load end : {}", auth);
        } catch (Exception e) {
            logger.error("school load error e = {}", e.getMessage());
        }
    }

    private <T> T load(String group, String dataId, Class<T> clazz, Listener listener) {
        try {
            String content = configUtil.getConfigService().getConfigAndSignListener(dataId, GROUP, 5000, listener);
            logger.info("YAML CONTENT : \n{}", content);
            T t = parseYaml(content,clazz);
            return t;
        } catch (Exception e) {
            logger.error("load error e = {}", e.getMessage());
        }
        return null;
    }

    private <T> T parseYaml(String content , Class<T> clazz){
        YAMLFactory factory = new YAMLFactory();
        try {
            logger.info("parse start...");
            YAMLParser parser = factory.createParser(content);
            T t = mapper.treeToValue(mapper.readTree(parser),clazz);
            logger.info("parse end...");
            return t ;
        } catch (IOException e) {
            logger.error("parse error : {}" , e.getMessage());
//            e.printStackTrace();
        }
        try {
            return clazz.newInstance() ;
        } catch (Exception e) {
            logger.error("new instance error");
        }
        return null ;
    }


    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        YAMLFactory yamlFactory = new YAMLFactory();
        YAMLParser parser = null;
        try {
            String content = "auth:\n" +
                    "  - id: A\n" +
                    "    name: 未注册用户\n" +
                    "    urls: \n" +
                    "      - /mainPage/*\n" +
                    "    children:\n" +
                    "      - id: A2\n" +
                    "        name: 普通用户\n" +
                    "        urls:\n" +
                    "          - /users/*\n" +
                    "          - -/admin/* # 代表不能进去admin 相关页面\n" +
                    "  - id: B\n" +
                    "    name: 教师\n" +
                    "    urls:\n" +
                    "     - /*\n" +
                    "     - -/admin/*\n" +
                    "  - id: C\n" +
                    "    name: 超级管理员\n" +
                    "    urls: \n" +
                    "     - /*";
            YAMLMapper mapper1 = new YAMLMapper();
            System.out.println(            mapper1.readValue(content,AuthDTO.class));
            parser = yamlFactory.createParser(content);
            Yaml yaml = new Yaml();
            TreeNode node = mapper.readTree(parser);
            AuthDTO dto = mapper.treeToValue(node, AuthDTO.class);
            System.out.println(mapper.writeValueAsString(dto));
            System.out.println(mapper.writeValueAsString(
                    yaml.loadAs(content, SchoolDTO.class)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
