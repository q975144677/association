package com.associtaion.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserDO {
String guid;
String name ;
String roleGuid ;
String schoolGuid ;
String username ;
String password;
String mobilePhone ;
Date createTime ;
Date updateTime ;

}
