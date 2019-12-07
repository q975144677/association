package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForAssociation;
import com.association.workflow.model.AssociationDO;
import com.associtaion.user.model.UserDO;
import component.PaginProto;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient("association-workflow")
public interface AssociationIface {

    /**
     * 创建社团(待审核)
     */
    @RequestMapping("createNewAssociation")
    Proto<Boolean> createNewAssociation(AssociationDO association);

    /**
     * 更新社团信息
     */
    @RequestMapping("updateAssociation")
    Proto<Boolean> updateAssociation(AssociationDO associationDO );

    /**
     * 查看社团成员
     */
    @RequestMapping("getAssociationMates")
    Proto<UserDO> getAssociationMates(ConditionForAssociation condition);

    /**
     * 查询社团
     */
    @RequestMapping("queryAssociation")
    PaginProto<List<AssociationDO>> queryAssociation(ConditionForAssociation condition);

}
