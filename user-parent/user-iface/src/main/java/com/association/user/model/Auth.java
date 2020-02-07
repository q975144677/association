package com.association.user.model;

import lombok.Data;

import java.util.List;
@Data
public class Auth {
    String name ;
    String id ;
    List<Auth> children ;
    List<String> urls;
}
