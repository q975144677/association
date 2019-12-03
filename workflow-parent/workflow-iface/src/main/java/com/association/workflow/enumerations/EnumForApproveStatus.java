package com.association.workflow.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum EnumForApproveStatus {
    START(1,"待审核"),
    APPROVING(2,"审核中"),
    SUCCESS(3,"审核通过"),
    FAILURE(4,"审核未通过"),
    WAITING(5,"代转账"),
    PAY_SUCCESS(6,"转账成功"),
    PAY_FAILURE(7,"转账失败"),
    UNKNOWN(-99,"未知");
    private int code ;
    private String info ;
    public static EnumForApproveStatus parse(int code){
            return Arrays.stream(EnumForApproveStatus.values()).filter(enumeration -> enumeration.getCode() == code).findAny().orElse(EnumForApproveStatus.UNKNOWN);
    }
}
