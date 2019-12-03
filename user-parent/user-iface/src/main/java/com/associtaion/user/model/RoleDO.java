package com.associtaion.user.model;

import lombok.Data;

import java.util.List;

@Data
public class RoleDO {
    private String guid ;
    private String name ;
    private List<String> auths;
    private String authJson;
}
