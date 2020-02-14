package com.association.workflow.model;

import com.association.workflow.enumerations.EnumForActivityStatus;
import com.association.workflow.enumerations.EnumForApproveStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityDO {
    private String guid ;
    private String name ;
    private Date createTime ;
    private Date updateTime ;
    private Integer schoolId ;
    private String createUserGuid;
    private String updateUserGuid;
    private String createUserName ;
    private String updateUserName;
    private String reason ;
    private String detail ;
    private Integer activityStatus;
    private String associationGuid ;
    private Date fromDate ;
    private Date toDate ;
    private String delayReason ;
    private String cancelReason ;
    private String approveMoney ;
    //pay 相关估计不能用了
    private String payAccount ;
    private Integer payType ;
    private Integer approveStatus ;

    public String getApproveStatusName() {
        return approveStatus == null ? null : EnumForApproveStatus.parse(approveStatus).getInfo();
    }
    public String getActivityStatusName(){
        return activityStatus == null ? null : EnumForActivityStatus.parse(activityStatus).getInfo();
    }
}
