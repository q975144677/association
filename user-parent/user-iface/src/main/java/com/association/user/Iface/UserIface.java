package com.association.user.Iface;

import com.association.user.condition.ConditionForUser;
import com.association.user.model.UserDO;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "association-user")
public interface UserIface {
    @RequestMapping("user/register")
    Proto<String> register(@RequestBody UserDO userDO);

    @RequestMapping("user/login")
    Proto<String> login(@RequestBody ConditionForUser condition);

    @RequestMapping("user/getUserByToken")
    Proto<UserDO> getUserByToken(@RequestParam("token") String token);

    @RequestMapping("user/updateUser")
    Proto<Boolean> updateUser(@RequestBody UserDO user);

    @RequestMapping("user/getUsers")
    Proto<List<UserDO>> getUsers(@RequestBody ConditionForUser condition);

    @RequestMapping("user/setToken")
    Proto<String> setToken(@RequestBody UserDO userDO);

    @RequestMapping("user/getUser")
    Proto<UserDO> getUser(@RequestBody ConditionForUser condition);

    @RequestMapping("user/checkToken")
    Proto<Boolean> checkToken(@RequestParam("token") String token);
    //todo 用户管理系统 cms 相关
    @RequestMapping("user/refrushToken")
    Proto<Boolean> refrushToken(@RequestParam("token") String token );
}
