package com.association.configsend.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RedisKeyEnum {
    USER_MESSAGE("USER_MESSAGE:%s-%s"), // 某用户未读消息 id set //废弃
    MESSAGE_ID("MESSAGE_ID:%s"),//废弃
    GROUP_MESSAGE("GROUP_MESSAGE:%s"),
    USER_MSGID("USER_MSGID:%s"),
    MSGID_MESSAGE("MSGID_MESSAGE:%s"); //废弃




    private String pattern;
}
