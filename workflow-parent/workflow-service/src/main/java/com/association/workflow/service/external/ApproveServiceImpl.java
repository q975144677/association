package com.association.workflow.service.external;

import com.association.workflow.condition.ConditionForApprove;
import com.association.workflow.iface.ApproveIface;
import com.association.workflow.model.ApproveDO;
import component.PaginProto;
import component.Proto;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class ApproveServiceImpl implements ApproveIface {
    @Override
    public Proto<Boolean> updateApprove(ApproveDO approve) {
        return null;
    }

    @Override
    public PaginProto<List<ApproveDO>> queryApprove(ConditionForApprove condition) {
        return null;
    }

    @Override
    public Proto<Boolean> addApprove(ApproveDO approve) {
        return null;
    }
}
