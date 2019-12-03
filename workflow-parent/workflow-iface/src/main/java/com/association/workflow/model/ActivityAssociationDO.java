package com.association.workflow.model;

import lombok.Data;

import java.util.Date;
@Data
public class ActivityAssociationDO {
    private String guid ;
    private Date createTime ;
    private String activityGuid ;
    private String associationGuid ;
}
