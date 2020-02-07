package com.association.workflow.model;

import lombok.Data;

import java.util.Date;

@Data
public class AssociationDO {
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
}
