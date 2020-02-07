package com.association.workflow.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ConditionForAssociation extends ConditionForPagin {
    String guid;
    private String name;
    private String subContent;
    private String avatarUrl;
    Date createTime;
    Date updateTime;
    private String createUserGuid;
    private String createUserName;
    private String associationLeaderGuid ;
    private Integer schoolId ;
    private String schoolName ;
    private List<String> associationGuids;
}
