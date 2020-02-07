package com.association.user.Iface;

import com.association.user.model.UserDO;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "association-user")
public interface GroupIface {
    @RequestMapping("getGroupUsers")
    Proto<List<UserDO>> getUsersByGroupId(@RequestParam("guid") String guid);
}
