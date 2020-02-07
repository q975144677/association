package com.association.workflow.service.external;

import com.association.notifacition.model.Message;
import com.association.workflow.iface.IMIface;
import com.association.user.model.UserDO;
import component.Proto;

import java.util.List;

public class IMServiceImpl implements IMIface {
    @Override
    public Proto<Boolean> sendMessage(Message msg) {
        return null;
    }

    @Override
    public Proto<List<Message>> pullMessageFromRedis(UserDO user) {
        return null;
    }

    @Override
    public Proto<Boolean> confirmMessage(Message msg) {
        return null;
    }
}
