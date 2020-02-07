package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForAssociation;
import com.association.workflow.condition.ConditionForAssociationUser;
import com.association.workflow.model.AssociationDO;
import com.association.user.model.UserDO;
import com.association.workflow.model.AssociationUserDO;
import component.PaginProto;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient("association-workflow")
public interface AssociationIface {

    /**
     * 创建社团(待审核)
     */
    @RequestMapping("createNewAssociation")
    Proto<Boolean> createNewAssociation(@RequestBody AssociationDO association);

    /**
     * 更新社团信息
     */
    @RequestMapping("updateAssociation")
    Proto<Boolean> updateAssociation(@RequestBody AssociationDO associationDO );

    /**
     * 查看社团成员
     */
    @RequestMapping("getAssociationMates")
    Proto<List<UserDO>> getAssociationMates(@RequestBody ConditionForAssociation condition);

    /**
     * 查询社团
     */
    @RequestMapping("queryAssociation")
    PaginProto<List<AssociationDO>> queryAssociation(@RequestBody ConditionForAssociation condition);

    /**
     * 查看社团关系
     */
    @RequestMapping("queryAssociationUser")
    Proto<List<AssociationUserDO>> queryAssociationUser(@RequestBody ConditionForAssociationUser condition) ;
}
