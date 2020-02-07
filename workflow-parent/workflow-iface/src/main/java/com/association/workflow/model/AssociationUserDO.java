package com.association.workflow.model;

import lombok.Data;

import java.util.Date;
@Data
public class AssociationUserDO {
private String guid ;
private String userGuid ;
private Date createDate ;
private Date updateDate ;
private String associationGuid;

}
