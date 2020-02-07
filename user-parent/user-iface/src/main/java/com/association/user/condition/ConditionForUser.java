package com.association.user.condition;

import component.ConditionForPagin;
import lombok.Data;

import java.util.List;

@Data
public class ConditionForUser extends ConditionForPagin {
    String guid;
    String name ;
    String roleGuid ;
    String schoolId ;
    String username ;
    String password;
    String mobilePhone;
    List<String> guids;
}
