package com.association.workflow.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.Date;
@Data
public class ConditionForComplain extends ConditionForPagin {
    private String guid ;
    private String detail ;
    private Date createTime ;
    private Date updateTime ;
    private String createUserGuid;
    private String updateUserGuid;
    private String createUserName ;
    private String updateUserName;
    private Integer complainType ;
}
