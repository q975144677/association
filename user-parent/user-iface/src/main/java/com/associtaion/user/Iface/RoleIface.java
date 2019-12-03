package com.associtaion.user.Iface;

import com.associtaion.user.model.Role;
import com.associtaion.user.model.RoleDO;
import component.Proto;

import java.util.List;

public interface RoleIface {
    Proto<List<String>> getUrlsByRole(RoleDO role);

    Proto<RoleDO> getRoleTree();

    Proto<Boolean> updateRole(RoleDO role);

    Proto<Boolean> createNewRole(RoleDO role);

    Proto<Boolean> deleteRole(RoleDO role);

}
