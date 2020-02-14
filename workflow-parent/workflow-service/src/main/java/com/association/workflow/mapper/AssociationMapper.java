package com.association.workflow.mapper;

import com.association.workflow.condition.ConditionForAssociation;
import com.association.workflow.model.AssociationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AssociationMapper {

    Boolean createNewAssociation(@Param("condition")AssociationDO associationDO);

    Boolean updateAssociation(@Param("condition")AssociationDO associationDO);

    Boolean deleteAssociation(@Param("guid")String guid);

    List<AssociationDO> queryAssociation(@Param("condition")ConditionForAssociation condition);

    Long countAssociation(@Param("condition")ConditionForAssociation condition);

    AssociationDO getAssociationSimple(@Param("condition")ConditionForAssociation condition);
}
