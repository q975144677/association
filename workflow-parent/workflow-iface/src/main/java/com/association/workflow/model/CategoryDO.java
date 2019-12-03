package com.association.workflow.model;

import lombok.Data;

import java.util.Date;
@Data
public class CategoryDO {
    String guid;
    String name ;
    Date createTime ;
    Date updateTime ;
}
