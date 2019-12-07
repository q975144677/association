package com.association.workflow.iface;

import com.association.notifacition.model.Message;
import com.associtaion.user.model.UserDO;
import component.Proto;

import java.util.List;

public interface IMIface {
    //todo 放在一起 用 枚举 分开
    /**
     * 用户A 发送给 用户B/群 消息
     */
    Proto<Boolean> sendMessage(Message msg);

    /**
     * 连接websocket时 拉取离线消息
     */
    Proto<List<Message>> pullMessageFromRedis(UserDO user);

    /**
     * 确认消息（删除缓存）
     */
    Proto<Boolean> confirmMessage(Message msg);

}
