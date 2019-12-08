package com.associtaion.user.Iface;

import com.associtaion.user.model.AuthDTO;
import com.associtaion.user.model.RoleDO;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "association-user")
public interface RoleIface {
    @RequestMapping("user/getUrlsByRole")
    Proto<List<String>> getUrlsByRole(RoleDO role);

    @RequestMapping("user/getRoleTree")
    Proto<AuthDTO> getRoleTree();

    @RequestMapping("user/updateRole")
    Proto<Boolean> updateRole(RoleDO role);

    @RequestMapping("user/createNewRole")
    Proto<Boolean> createNewRole(RoleDO role);

    @RequestMapping("user/deleteRole")
    Proto<Boolean> deleteRole(RoleDO role);

}
