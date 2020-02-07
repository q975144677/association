package com.association.workflow.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ConditionForApprove extends ConditionForPagin {
    private String guid;
    private String approveGuid ;
    private Integer approveStatus;
    private Integer approveType;
    private Date createTime;
    private Date updateTime;
    private String createUserGuid;
    private String updateUserGuid;
    private String createUserName;
    private String updateUserName;
    private String reason;
    private Integer schoolId;
    private List<String> approveGuids;
    private List<Integer> approveStatusList ;
}
