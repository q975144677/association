package com.association.workflow.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.Date;
@Data
public class ConditionForAssociationUser extends ConditionForPagin {
    private String guid ;
    private String userGuid ;
    private Date createDate ;
    private Date updateDate ;
    private String associationGuid;

}
