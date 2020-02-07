package com.association.user.mapper;

import com.association.user.condition.ConditionForRole;
import com.association.user.model.RoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    Boolean createNewRole(@Param("condition") RoleDO role);

    Boolean updateRole(@Param("condition") RoleDO role);

    List<RoleDO> getRoleList(@Param("condition") ConditionForRole condition,@Param("limit") Integer limit ,@Param("offset") Integer offset);

    Boolean deleteRole(@Param("guid")String guid);

    RoleDO getRoleByGuid(@Param("guid")String guid);
}
