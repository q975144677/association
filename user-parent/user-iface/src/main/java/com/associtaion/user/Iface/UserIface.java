package com.associtaion.user.Iface;

import com.associtaion.user.condition.ConditionForUser;
import com.associtaion.user.model.UserDO;
import component.Proto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "association-user")
public interface UserIface {
    @RequestMapping("user/register")
    Proto<String> register(ConditionForUser condition);

    @RequestMapping("user/login")
    Proto<String> login(ConditionForUser condition);

    @RequestMapping("user/getUserByToken")
    Proto<UserDO> getUserByToken(String token);

    //todo 用户管理系统 cms 相关
}
