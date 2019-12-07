package com.association.workflow.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.Date;

@Data
public class ConditionForCategory extends ConditionForPagin {
    String id;
    String name ;

}
