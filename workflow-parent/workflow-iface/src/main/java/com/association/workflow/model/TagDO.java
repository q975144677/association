package com.association.workflow.model;

import lombok.Data;

import java.util.Date;

//todo 感觉可以放在 es 或者 缓存里
@Data
public class TagDO {
    private String guid ;
    private String name ;
    private Date createTime ;
    private String createUserGuid;
    private String createUserName ;
}
