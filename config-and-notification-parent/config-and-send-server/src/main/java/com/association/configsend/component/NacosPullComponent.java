package com.association.configsend.component;

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
            logger.error("parse error...");
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
            String content = "areas: \n" +
                    "  - code: 1 \n" +
                    "    name: 上海\n" +
                    "    schools: \n" +
                    "      - code: 1\n" +
                    "        name: 上海电力大学\n" +
                    "      - code: 2 \n" +
                    "        name: 上海大学\n" +
                    "      - code: 3 \n" +
                    "        name: 上海还行大学\n" +
                    "  - code: 2 \n" +
                    "    name: 北京\n" +
                    "    schools: \n" +
                    "      - code: 4\n" +
                    "        name: 北京大学\n" +
                    "      - code: 5 \n" +
                    "        name: 清华大学\n" +
                    "  - code: 3 \n" +
                    "    name: 浙江\n" +
                    "    schools: \n" +
                    "      - code: 6\n" +
                    "        name: 浙江大学";
            parser = yamlFactory.createParser(content);
            Yaml yaml = new Yaml();
            TreeNode node = mapper.readTree(parser);
            SchoolDTO dto = mapper.treeToValue(node, SchoolDTO.class);
            System.out.println(mapper.writeValueAsString(dto));
            System.out.println(mapper.writeValueAsString(
                    yaml.loadAs(content, SchoolDTO.class)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
