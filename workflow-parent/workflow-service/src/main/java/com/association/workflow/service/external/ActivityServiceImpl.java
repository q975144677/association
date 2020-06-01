package com.association.workflow.service.external;

import com.association.user.Iface.UserIface;
import com.association.user.condition.ConditionForUser;
import com.association.user.model.UserDO;
import com.association.workflow.condition.ConditionForActivity;
import com.association.workflow.condition.ConditionForActivityUser;
import com.association.workflow.iface.ActivityIface;
import com.association.workflow.mapper.ActivityMapper;
import com.association.workflow.mapper.ActivityUserMapper;
import com.association.workflow.model.ActivityDO;
import com.association.workflow.model.ActivityUserDO;
import component.BasicComponent;
import component.PaginProto;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ActivityServiceImpl extends BasicComponent implements ActivityIface {

    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    AssociationServiceImpl associationService;
//    @Autowired
//    ActivityMapper activityMapper;
    @Autowired
    ActivityUserMapper activityUserMapper;
    @Autowired
    UserIface userIface;

    @Override
    public Proto<Boolean> createNewActivity(ActivityDO activity) {
        activity.setGuid(UUID.randomUUID().toString().replaceAll("-", ""));
        return getResult(activityMapper.createNewActivity(activity));
    }

    @Override
    public Proto<Boolean> updateActivity(ActivityDO activity) {
        return getResult(activityMapper.updateActivity(activity));
    }

    @Override
    public PaginProto<List<ActivityDO>> queryActivity(ConditionForActivity condition) {
        condition.prepare(null);
        return getPaginResult(activityMapper.queryActivity(condition), new PaginProto.Page());
    }

    @Override
    public Proto<List<UserDO>> queryActivityUsers(ConditionForActivity condition) {
        ConditionForActivityUser conditionForActivityUser = new ConditionForActivityUser();
        conditionForActivityUser.setActivityGuid(condition.getGuid());
        List<ActivityUserDO> activityUserDOS = activityUserMapper.findActivityUser(conditionForActivityUser);
        List<String> userGuids = activityUserDOS.stream().map(au -> au.getUserGuid()).collect(Collectors.toList());
        ConditionForUser conditionForUser = new ConditionForUser();
        conditionForUser.setGuids(userGuids);
        return getResult(userIface.getUsers(conditionForUser));
    }

    @Override
    public Proto<Boolean> quitActivity(ConditionForActivityUser condition) {
        return getResult(activityUserMapper.deleteRef(condition.getUserGuid() , condition.getActivityGuid()));
    }

    @Override
    public Proto<Boolean> joinActivity(ActivityUserDO activityUserDO) {
        return getResult(activityUserMapper.save(activityUserDO));
    }
public static void main(String[] args){
    System.out.println("1.'".indexOf("\\."));
}
//    public static void main(String[] args){
//    System.out.println(11);
//}
}
