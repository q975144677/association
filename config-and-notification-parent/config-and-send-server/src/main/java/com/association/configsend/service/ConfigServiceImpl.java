package com.association.configsend.service;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.association.config.iface.ConfigIface;
import com.association.config.model.AuthDTO;
import com.association.config.model.CategoryDTO;
import com.association.config.model.SchoolDTO;
import component.BasicComponent;
import component.BasicService;
import component.Proto;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigServiceImpl extends BasicService implements ConfigIface  {
    @Override
    public Proto<SchoolDTO> schools() {
        return getResult(SchoolDTO.getInstance());
    }

    @Override
    public Proto<AuthDTO> auth() {
        return getResult(AuthDTO.getInstance());
    }

    @Override
    public Proto<CategoryDTO> category() {
        return getResult(CategoryDTO.getInstance());
    }
}
