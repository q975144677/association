package com.association.workflow.service.external;

import com.association.workflow.condition.ConditionForActivity;
import com.association.workflow.iface.ActivityIface;
import com.association.workflow.model.ActivityDO;
import com.associtaion.user.model.UserDO;
import component.PaginProto;
import component.Proto;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class ActivityServiceImpl implements ActivityIface {
    @Override
    public Proto<Boolean> createNewActivity(ActivityDO activity) {
        return null;
    }

    @Override
    public Proto<Boolean> updateActivity(ActivityDO activity) {
        return null;
    }

    @Override
    public PaginProto<List<ActivityDO>> queryActivity(ConditionForActivity condition) {
        return null;
    }

    @Override
    public PaginProto<List<UserDO>> queryActivityUsers(ConditionForActivity condition) {
        return null;
    }
}
