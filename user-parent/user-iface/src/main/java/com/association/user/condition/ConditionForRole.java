package com.association.user.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.List;
@Data
public class ConditionForRole extends ConditionForPagin {
    private String guid ;
    private String name ;
    private List<String> auths;
    private String authJson;
}
