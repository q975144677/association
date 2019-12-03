package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForApprove;
import com.association.workflow.model.ApproveDO;
import component.PaginProto;
import component.Proto;

import java.util.List;

public interface ApproveIface {

    /**
     * 审核
     */
    Proto<Boolean> updateApprove(ApproveDO approve);

    /**
     * 查询审核
     */
    PaginProto<List<ApproveDO>> queryApprove(ConditionForApprove condition);

    /**
     * 申请（加入社团等
     */
    Proto<Boolean> addApprove(ApproveDO approve);

//    /**
//     * 查询申请
//     */
//    PaginProto<List<ApproveDO>> queryApprove(ConditionForApprove condition);
}
