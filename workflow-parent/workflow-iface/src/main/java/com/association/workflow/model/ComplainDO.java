package com.association.workflow.model;

import com.association.workflow.enumerations.EnumForComplainType;
import lombok.Data;

import java.util.Date;
//todo 重要性不高
@Data
public class ComplainDO {
    private String guid ;
    private String detail ;
    private Date createTime ;
    private Date updateTime ;
    private String createUserGuid;
    private String updateUserGuid;
    private String createUserName ;
    private String updateUserName;
    private Integer complainType ;

    public String getComplainTypeName(){
        return complainType == null ? null: EnumForComplainType.parse(complainType).getInfo();
    }
}
