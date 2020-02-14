package com.association.admin.controller;

import com.association.config.model.MessageDTO;
import com.association.notifacition.iface.NotifactionIface;
import com.association.workflow.iface.AssociationIface;
import component.BasicComponent;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("msg")
public class IMController extends BasicComponent {
    @Autowired
    NotifactionIface notifactionIface;
@Autowired
AssociationIface associationIface;
    @PostMapping("getMsgs")
    public Proto<?> getMsgs(HttpServletRequest request) {
        Operator operator = getOperator(request);
        return getResult(notifactionIface.getMessage(operator.getGuid()));
    }

    @PostMapping("sendMsg")
    public Proto<?> sendMsg(@RequestBody MessageDTO msg, HttpServletRequest request) {
        Operator operator = getOperator(request);
        msg.setFrom(operator.getGuid());
        msg.setFromName(operator.getName());
        msg.setAvatar(operator.getAvatar());
        return getResult(notifactionIface.sendMessage(msg));
    }
    @PostMapping("pull")
    public Proto<?> pull(@RequestBody Map<String, List<String>> map , HttpServletRequest request){
        Operator operator = getOperator(request);
        return notifactionIface.pullMessage(operator.getGuid());
    }
    @PostMapping("delMsg")
    public Proto<?> delMsg(@RequestBody MessageDTO messageDTO, HttpServletRequest request) {
        Operator operator = getOperator(request);
        messageDTO.setTo(operator.getGuid());
        return getResult(notifactionIface.delMsgRedisKey(messageDTO));
    }

    @PostMapping("delMsgNew")
    public Proto<Boolean> delMsgNew(HttpServletRequest request) {
        Operator operator = getOperator(request);
        String guid = operator.getGuid();
        return getResult(notifactionIface.delMsgRedisKeyByUserGuid(guid));
    }
}
