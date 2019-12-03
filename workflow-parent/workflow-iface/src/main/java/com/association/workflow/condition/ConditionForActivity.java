package com.association.workflow.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ConditionForActivity extends ConditionForPagin {
    private String guid;
    private String name;
    private Date createTime;
    private Date updateTime;
    private String createUserGuid;
    private String updateUserGuid;
    private String createUserName;
    private String updateUserName;
    private String reason;
    private String detail;
    private Integer activityStatus;
    private Date fromDate;
    private Date toDate;
    private String delayReason;
    private String cancelReason;
    private String approveMoney;
    private String payAccount;
    private Integer payType;
    private Integer approveStatus;
    private String associationGuid;
    private List<String> associationGuids;
}
