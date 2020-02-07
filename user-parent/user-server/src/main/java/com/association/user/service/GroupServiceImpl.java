package com.association.user.service;

import com.association.user.keeper.UserKeeper;
import com.association.user.Iface.GroupIface;
import com.association.user.condition.ConditionForUser;
import com.association.user.model.UserDO;
import component.BasicComponent;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class GroupServiceImpl extends BasicComponent implements GroupIface {

    @Autowired
    UserKeeper userKeeper ;
    @Override
    public Proto<List<UserDO>> getUsersByGroupId(String guid) {
        List<String> userGuids = userKeeper.getUserGuidByGroupGuid(guid);
        if(CollectionUtils.isEmpty(userGuids)){
            return getResult(Collections.EMPTY_LIST);
        }
        ConditionForUser conditionForUser = new ConditionForUser();
        conditionForUser.setGuids(userGuids);
        List<UserDO> users = userKeeper.getUsers(conditionForUser);
        return getResult(users);
    }
}
