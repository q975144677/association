package com.association.config.model;

import com.associtaion.user.model.Auth;
import lombok.Data;

import java.util.List;

@Data
public class AuthDTO {
    private static AuthDTO instance ;
    private Auth root ;
}
