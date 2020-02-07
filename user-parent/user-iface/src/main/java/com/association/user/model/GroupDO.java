package com.association.user.model;

import lombok.Data;

import java.util.List;

@Data
public class GroupDO {
    private String guid ;
    private String name ;
    private List<String> groupMates;
}
