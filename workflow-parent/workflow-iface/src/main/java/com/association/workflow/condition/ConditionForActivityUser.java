package com.association.workflow.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.Date;
@Data
public class ConditionForActivityUser extends ConditionForPagin {
    private String guid ;
    private String userGuid ;
    private String activityGuid ;
}
