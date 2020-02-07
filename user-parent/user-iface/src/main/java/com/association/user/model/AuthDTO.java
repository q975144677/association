package com.association.user.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthDTO {
    @Getter
    @Setter
    private static AuthDTO instance ;
    private Auth auth ;
}
