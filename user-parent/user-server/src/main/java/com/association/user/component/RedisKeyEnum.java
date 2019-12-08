package com.association.user.component;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RedisKeyEnum {
    USER_TOKEN("USER_TOKEN:%s"),
    REDIS_LOCK_FOR_REGISTER("REDIS_LOCK_FOR_REGISTER:%s");

    private String pattern;
}
