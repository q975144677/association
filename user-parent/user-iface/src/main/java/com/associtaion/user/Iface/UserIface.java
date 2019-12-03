package com.associtaion.user.Iface;

import com.associtaion.user.condition.ConditionForUser;
import com.associtaion.user.model.UserDO;
import component.Proto;

public interface UserIface {
    Proto<String> register(ConditionForUser condition);

    Proto<String> login(ConditionForUser condition);

    Proto<UserDO> getUserByToken(String token);

    //todo 用户管理系统 cms 相关
}
