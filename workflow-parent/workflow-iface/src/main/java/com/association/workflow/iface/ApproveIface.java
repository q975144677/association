package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForApprove;
import com.association.workflow.model.ApproveDO;
import component.PaginProto;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient("association-workflow")
public interface ApproveIface {

    /**
     * 审核
     */
    @RequestMapping("updateApprove")
    Proto<Boolean> updateApprove(ApproveDO approve);

    /**
     * 查询审核
     */
    @RequestMapping("queryApprove")
    PaginProto<List<ApproveDO>> queryApprove(ConditionForApprove condition);

    /**
     * 申请（加入社团等
     */
    @RequestMapping("addApprove")
    Proto<Boolean> addApprove(ApproveDO approve);

//    /**
//     * 查询申请
//     */
//    PaginProto<List<ApproveDO>> queryApprove(ConditionForApprove condition);
}
