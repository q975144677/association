package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForActivity;
import com.association.workflow.model.ActivityDO;
import com.associtaion.user.model.UserDO;
import component.PaginProto;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient(name = "association-workflow")
public interface ActivityIface {
    /**
     * 新增活动(待审核)
     */
    @RequestMapping("createNewActivity")
    Proto<Boolean> createNewActivity(ActivityDO activity);

    /**
     * 更新活动
     */
    @RequestMapping("updateActivity")
    Proto<Boolean> updateActivity(ActivityDO activity);

    /**
     * 查询活动
     */
    @RequestMapping("queryActivity")
    PaginProto<List<ActivityDO>> queryActivity(ConditionForActivity condition);

    /**
     * 查找参与活动的用户
     */
    @RequestMapping("queryActUser")
    PaginProto<List<UserDO>> queryActivityUsers(ConditionForActivity condition);


}
