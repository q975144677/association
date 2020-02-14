package com.association.notifacition.iface;

import com.association.config.model.MessageDTO;
import com.association.notifacition.model.Message;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "association-config")
public interface NotifactionIface {
    @RequestMapping("sdm")
    Proto<Boolean> sendMessage(@RequestBody MessageDTO message);

    @RequestMapping("pkl")
    Proto<Set<MessageDTO>> pullMessage(@RequestParam("guid") String guid);

    @RequestMapping("gmsg")
    Proto<Set<?>> getMessage(@RequestParam("guid") String guid);

    @RequestMapping("dmsguser")
    Proto<Boolean> delMsgRedisKeyByUserGuid(@RequestParam("guid") String guid);

    @RequestMapping("dmsg")
    Proto<Boolean> delMsgRedisKey(@RequestBody MessageDTO message);
}
