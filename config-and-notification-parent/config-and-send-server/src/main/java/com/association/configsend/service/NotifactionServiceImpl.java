package com.association.configsend.service;

import com.alibaba.fastjson.JSONObject;
import com.association.config.model.MessageDTO;
import com.association.configsend.component.RedisUtil;
import com.association.notifacition.enumeration.EnumForMessageType;
import com.association.notifacition.iface.NotifactionIface;
import com.association.workflow.condition.ConditionForAssociationUser;
import com.association.workflow.iface.AssociationIface;
import com.association.workflow.model.AssociationUserDO;
import com.associationNew.WebsocketEndPointNew;
import component.BasicComponent;
import component.Proto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RestController
public class NotifactionServiceImpl extends BasicComponent implements NotifactionIface {
    @Autowired
    WebsocketEndPointNew handler;
    @Autowired
    RedisUtil redis;
    @Autowired
    AssociationIface associationIface;
    public static final String messageKey = "MESSAGE:%s";

    @Override
    public Proto<Boolean> sendMessage(MessageDTO message) {
        if (Integer.valueOf(EnumForMessageType.ONE_TO_ASSOCIATION.getCode()).equals(message.getMsgType())) {
            String userGuid = message.getFrom();
            if (StringUtils.isEmpty(message.getGuid())) {
                message.setGuid(UUID.randomUUID().toString());
            }
            String associationGuid = message.getTo();
            ConditionForAssociationUser condition = new ConditionForAssociationUser();
            condition.setAssociationGuid(associationGuid);
            Proto<List<AssociationUserDO>> proto = associationIface.queryAssociationUser(condition);
            if (proto != null && proto.getData() != null) {
                message.setToGuids(proto.getData().stream().map(associationUserDO -> associationUserDO.getUserGuid()).collect(Collectors.toList()));
            }
            for (String toGuid : message.getToGuids()) {
                String keyNew = String.format(messageKey, toGuid);
                redis.sadd(keyNew, JSONObject.toJSONString(message));
                handler.sendMessage(JSONObject.toJSONString(message), toGuid);
            }
            return getResult(true);
        }
        return getResult(Boolean.FALSE);
    }

    @Override
    public Proto<Set<MessageDTO>> pullMessage(String userGuid) {
//        ConditionForAssociationUser condition = new ConditionForAssociationUser();
//        condition.setUserGuid(userGuid);
//        Proto<List<AssociationUserDO>> proto =
//                associationIface.queryAssociationUser(condition);
//        if(proto == null || proto.getData() == null){
//            return getResult(Collections.EMPTY_SET);
//        }
//        List<String> assoIds = proto.getData().stream().map(x->x.getAssociationGuid()).collect(Collectors.toList());
        Set<Object> result = new HashSet<>();
//        for(String guid : assoIds) {
        String key = String.format(messageKey, userGuid);
        Set<Object> set = redis.sGet(key);
        redis.del(userGuid);
        result.addAll(set);
//        }
        return getResult(result.stream()
                .map(obj -> JSONObject.parseObject(String.valueOf(obj), MessageDTO.class))
                .collect(Collectors.toSet()));
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