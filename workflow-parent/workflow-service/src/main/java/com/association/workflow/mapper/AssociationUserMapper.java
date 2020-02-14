package com.association.workflow.mapper;

import com.association.workflow.condition.ConditionForActivityUser;
import com.association.workflow.condition.ConditionForAssociationUser;
import com.association.workflow.model.ActivityUserDO;
import com.association.workflow.model.AssociationUserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AssociationUserMapper {
    Boolean save(@Param("condition") AssociationUserDO activityUserDO);

    Boolean delete(@Param("guid") String guid);

    List<AssociationUserDO> findAssociationUser(@Param("condition") ConditionForAssociationUser condition);

    Boolean deleteRef(@Param("associationGuid") String associationGuid, @Param("userGuid") String userGuid);
}
