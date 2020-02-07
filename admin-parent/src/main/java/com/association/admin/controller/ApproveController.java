package com.association.admin.controller;

import com.association.workflow.condition.ConditionForApprove;
import com.association.workflow.enumerations.EnumForApproveType;
import com.association.workflow.iface.ApproveIface;
import com.association.workflow.model.ApproveDO;
import component.BasicComponent;
import component.HttpRequestException;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("approve")
public class ApproveController extends BasicComponent {
    @Autowired
    ApproveIface approveIface;

    @PostMapping("confirm")
    public Proto<?> confirm(ApproveDO approve, HttpServletRequest request) {
        if(StringUtils.isEmpty(approve.getGuid()) || approve.getApproveType() == null){
            throw new HttpRequestException(HttpRequestException.PARAM_ERROR, HttpStatus.OK.value());
        }
        //todo 鉴权
        Operator operator = getOperator(request);
        approve.setUpdateUserGuid(operator.getGuid());
        approve.setUpdateUserName(operator.getName());
        return getResult(approveIface.updateApprove(approve));
    }


    @PostMapping("apply")
    public Proto<?> apply(ApproveDO approveDO , HttpServletRequest request){
        Operator operator = getOperator(request);
        approveDO.setCreateUserGuid(operator.getGuid());
        approveDO.setCreateUserName(operator.getName());
        return getResult(approveIface.addApprove(approveDO));
    }

    @PostMapping("approves")
    public Proto<?> approves(@RequestBody ConditionForApprove condition,HttpServletRequest request){
        Operator operator = getOperator(request);
        condition.setCreateUserGuid(operator.getGuid());
        return getResult(approveIface.queryApprove(condition));
    }
}
