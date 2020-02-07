package com.association.user.Iface;

import com.association.user.model.Auth;
import com.association.user.model.AuthDTO;
import com.association.user.model.RoleDO;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "association-user")
public interface RoleIface {
    @RequestMapping("user/getUrlsByRole")
    Proto<List<String>> getUrlsByRole(RoleDO role);

    @RequestMapping("user/getRoleTree")
    Proto<AuthDTO> getRoleTree();

    @RequestMapping("user/updateRole")
    Proto<Boolean> updateRole(@RequestBody RoleDO role);

    @RequestMapping("user/createNewRole")
    Proto<Boolean> createNewRole(@RequestBody RoleDO role);

    @RequestMapping("user/deleteRole")
    Proto<Boolean> deleteRole(@RequestBody RoleDO role);

    @RequestMapping("user/getAuthByGuid")
    Proto<List<Auth>> getAuthByGuid(@RequestParam("guid") String guid);
}
