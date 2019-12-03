package com.association.workflow.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum EnumForPayType {
    UNNECESSARY(1,"无需申请活动资金"),
    ALIPAY(2,"支付宝"),
    WECHAR(3,"微信"),
    BANK_CARD(4,"银行卡"),
    UNKNOWN(-99,"未知");
private int code ;
private String info ;
    public static EnumForPayType parse(int code){
        return Arrays.stream(EnumForPayType.values()).filter(enumeration -> enumeration.getCode() == code).findAny().orElse(EnumForPayType.UNKNOWN);
    }
}
