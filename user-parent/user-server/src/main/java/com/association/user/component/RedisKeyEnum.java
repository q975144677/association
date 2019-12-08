package com.association.user.component;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RedisKeyEnum {
    USER_TOKEN("USER_TOKEN:%s");

    private String pattern;
}
