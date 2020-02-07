package com.association.user.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDO {
    String guid;
    String name;
    String roleGuid;
    Integer schoolId;
    String username;
    String password;
    String mobilePhone;
    Date createTime;
    Date updateTime;
    List<String> associations;

    String avatar;
}
