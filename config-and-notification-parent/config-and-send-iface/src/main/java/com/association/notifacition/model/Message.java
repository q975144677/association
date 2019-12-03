package com.association.notifacition.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Message {
    //todo toUserGuid 为 redis key 做 set
    //todo associationGuid 转化为 群成员 即 List<String> guids
    /**
     * 消息本身guid
     */
    private String guid ;

    /**
     * 发送方
     */
    private String fromUserGuid ;

    /**
     * 接收方(单发)
     */
    private String toUserGuid ;

    /**
     * 群聊
     */
    private String associationGuid ;

    /**
     * 发送时间
     */
    private Date createTime ;

    /**
     * 消息内容
     */
    private String msg;


}
