package com.association.config.model;

import com.associtaion.user.model.Auth;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class AuthDTO {
    @Getter
    @Setter
    private static AuthDTO instance ;
    private Auth auth ;
}
