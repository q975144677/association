package com.association.workflow.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumForReidsKey {
    APPROVE_LOCK ("approve_lock:%s");
    private String pattern ;
}
