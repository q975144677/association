package com.association.workflow.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum EnumForComplainType {
    WASTE(1,"浪费公款"),
    BUREAUCRAT(2,"官僚主义"),
    MEANINGLESS(3,"无意义社团"),
    SEXY(4,"传播淫秽"),
    AGAINST(5,"传播反动言论"),
    OTHER(9,"其他"),
UNKNOWN(-99 , "未知");
;
private int code ;
private String info ;
    public static EnumForComplainType parse(int code){
        return Arrays.stream(EnumForComplainType.values()).filter(enumeration -> enumeration.getCode() == code).findAny().orElse(EnumForComplainType.UNKNOWN);
    }
}
