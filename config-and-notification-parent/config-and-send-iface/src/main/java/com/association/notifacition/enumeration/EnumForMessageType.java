package com.association.notifacition.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EnumForMessageType {
    ONE_TO_ONE(1,"私聊"),
    ONE_TO_ASSOCIATION(2,"社团消息"),
    ONE_TO_ACTIVITY(3,"活动消息"),
    UNKNOWN(-99,"位置");

    private int code;
    private String info;
    public static EnumForMessageType parse(int code){
            return Arrays.stream(EnumForMessageType.values()).filter(enumeration -> enumeration.getCode() == code).findAny().orElse(EnumForMessageType.UNKNOWN);
        }
}
