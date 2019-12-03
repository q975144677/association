package com.association.notifacition.iface;

import com.association.notifacition.model.Message;
import component.Proto;

public interface NotifactionIface {
    Proto<Boolean> sendMessage(Message message);
}
