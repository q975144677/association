package com.associtaion.user.condition;

import component.ConditionForPagin;
import lombok.Data;

@Data
public class ConditionForUser extends ConditionForPagin {
    String guid;
    String name ;
    String roleGuid ;
    String schoolGuid ;
    String username ;
    String password;
}
