package com.association.workflow.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum EnumForApproveType {
    JOIN_ASSOCIATION(1, "加入社团请求"),
    HOLD_ACTIVITY(2,"发起活动请求"),
    COMPLAIN(3,"申诉请求"),
    CERTIFIED(4,"资格证请求"), // 学校学生认证 需上传 学生证照片&身份证照片
    UNKNOWN(-99,"未知")
    ;
    private int code;
    private String info;

    public static EnumForApproveType parse(int code){
        try {
           return Arrays.stream(EnumForApproveType.values()).filter(enumeration -> enumeration.getCode() == code).findAny().orElse(EnumForApproveType.UNKNOWN);
        }catch (Exception e){
            return EnumForApproveType.UNKNOWN;
        }
    }

}
