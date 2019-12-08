package com.association.user.service;

import com.associtaion.user.Iface.RoleIface;
import com.associtaion.user.model.AuthDTO;
import com.associtaion.user.model.RoleDO;
import component.BasicService;
import component.Proto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleServiceImpl extends BasicService implements RoleIface {

    @Override
    public Proto<List<String>> getUrlsByRole(RoleDO role) {
        return null;
    }

    @Override
    public Proto<AuthDTO> getRoleTree() {
        return null;
    }

    @Override
    public Proto<Boolean> updateRole(RoleDO role) {
        return null;
    }

    @Override
    public Proto<Boolean> createNewRole(RoleDO role) {
        return null;
    }

    @Override
    public Proto<Boolean> deleteRole(RoleDO role) {
        return null;
    }
}
