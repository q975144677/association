package com.associtaion.user.model;

import lombok.Data;

import java.util.List;
@Data
public class Auth {
    String name ;
    String code ;
    List<Auth> children ;
    List<String> url;
}
