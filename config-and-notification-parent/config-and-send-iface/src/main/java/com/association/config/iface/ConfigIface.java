package com.association.config.iface;

import com.association.config.model.AuthDTO;
import com.association.config.model.CategoryDTO;
import com.association.config.model.SchoolDTO;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "association-config")
public interface ConfigIface {
    @RequestMapping("config/schools")
    Proto<SchoolDTO> schools();

    @RequestMapping("config/auth")
    Proto<AuthDTO> auth();

    @RequestMapping("config/category")
    Proto<CategoryDTO> category();
}
