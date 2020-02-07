package com.association.configsend.service;

import com.association.config.model.MessageDTO;
import com.association.configsend.component.RedisUtil;
import com.association.notifacition.enumeration.EnumForMessageType;
import com.association.notifacition.iface.NotifactionIface;
import com.association.notifacition.model.Message;
import component.BasicComponent;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;

import java.util.List;
import java.util.Set;

@Service
public class NotifactionServiceImpl extends BasicComponent implements NotifactionIface {
    @Autowired
    WebsocketEndPoint handler;
    @Autowired
    RedisUtil redis;
    public static final String messageKey = "MESSAGE:%s";

    @Override
    public Proto<Boolean> sendMessage(MessageDTO message) {
        if (Integer.valueOf(EnumForMessageType.ONE_TO_ASSOCIATION.getCode()).equals(message.getMsgType())) {
            String userGuid = message.getFrom();
            String key = String.format(messageKey, userGuid);
            String associationGuid = message.getTo();
            Set<Object> set = redis.sGet(key);
            set.add(message);
            redis.sSet(key, set);
          //  Boolean result = handler.sendMessage(message);
            return getResult(true);
        }
        return getResult(Boolean.FALSE);
    }

    @Override
    public Proto<Set<?>> getMessage(String guid) {
        String key = String.format(messageKey, guid);
        Set<Object> set = redis.sGet(key);
        return getResult(set);
    }

    @Override
    public Proto<Boolean> delMsgRedisKeyByUserGuid(String guid) {
        String key = String.format(messageKey, guid);
        redis.del(key);
        return getResult(Boolean.TRUE);
    }

    @Override
    public Proto<Boolean> delMsgRedisKey(MessageDTO message) {
        return null;
    }
}

/**
 * redis : =>     key : userGuid , value : [message1 , message2]
 * 1 . sendMessage => 判断是否在线 。redis sadd 在线则发送 ，
 */


//redis msg key 设置
// 1. 获取发给我的消息 => userGuid : msg
// 2. 根据msgGuid删除消息 msgGuid : msg
// key_msgGuid:msg

// userGuid : [set][msgGuid]