package com.association.workflow.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum EnumForActivityStatus {
    WAITING(1, "暂未开始"),
    STARTING(2, "正在进行"),
    END(3, "已经结束"),
    DELAY(4, "延迟举行"),
    CANCEL(5,"因故取消"),
    CANT_SEE(6,"不可见"),
    UNKNOWN(-99, "未知");
    private int code;
    private String info;

    public static EnumForActivityStatus parse(int code) {
        return Arrays.stream(EnumForActivityStatus.values()).filter(enumeration -> enumeration.getCode() == code).findAny().orElse(EnumForActivityStatus.UNKNOWN);
    }
}
