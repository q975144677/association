package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForAssociation;
import com.association.workflow.model.AssociationDO;
import com.associtaion.user.model.UserDO;
import component.PaginProto;
import component.Proto;

import java.util.List;

public interface AssociationIface {

    /**
     * 创建社团(待审核)
     */
    Proto<Boolean> createNewAssociation(AssociationDO association);

    /**
     * 更新社团信息
     */
    Proto<Boolean> updateAssociation(AssociationDO associationDO );

    /**
     * 查看社团成员
     */
    Proto<UserDO> getAssociationMates(ConditionForAssociation condition);

    /**
     * 查询社团
     */
    PaginProto<List<AssociationDO>> queryAssociation(ConditionForAssociation condition);

}
