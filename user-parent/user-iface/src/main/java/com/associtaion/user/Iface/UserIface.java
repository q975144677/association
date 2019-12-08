package com.associtaion.user.Iface;

import com.associtaion.user.condition.ConditionForUser;
import com.associtaion.user.model.UserDO;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "association-user")
public interface UserIface {
    @RequestMapping("user/register")
    Proto<String> register(@RequestBody UserDO userDO);

    @RequestMapping("user/login")
    Proto<String> login(@RequestBody ConditionForUser condition);

    @RequestMapping("user/getUserByToken")
    Proto<UserDO> getUserByToken(@RequestParam String token);

    @RequestMapping("user/updateUser")
    Proto<Boolean> updateUser(@RequestBody UserDO user);
    //todo 用户管理系统 cms 相关
}
