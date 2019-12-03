package com.association.workflow.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum EnumForVoteType {
    ASSOCIATION(1,"社团"),
    ACTIVITY(2,"活动"),
    TAG(3,"标签"),
    UNKNOWN(-99,"未知");
    private int code ;
    private String info ;
    public static EnumForVoteType parse(int code){
        return Arrays.stream(EnumForVoteType.values()).filter(enumeration -> enumeration.getCode() == code).findAny().orElse(EnumForVoteType.UNKNOWN);
    }
}
