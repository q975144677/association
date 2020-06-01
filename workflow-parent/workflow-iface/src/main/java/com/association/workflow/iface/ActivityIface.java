package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForActivity;
import com.association.workflow.condition.ConditionForActivityUser;
import com.association.workflow.model.ActivityDO;
import com.association.user.model.UserDO;
import com.association.workflow.model.ActivityUserDO;
import component.PaginProto;
import component.Proto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient(name = "association-workflow")
public interface ActivityIface {
    /**
     * 新增活动(待审核)
     */
    @RequestMapping("createNewActivity")
    Proto<Boolean> createNewActivity(@RequestBody ActivityDO activity);

    /**
     * 更新活动
     */
    @RequestMapping("updateActivity")
    Proto<Boolean> updateActivity(@RequestBody ActivityDO activity);

    /**
     * 查询活动
     */
    @RequestMapping("queryActivity")
    PaginProto<List<ActivityDO>> queryActivity(@RequestBody ConditionForActivity condition);

    /**
     * 查找参与活动的用户
     */
    @RequestMapping("queryActUser")
    Proto<List<UserDO>> queryActivityUsers(@RequestBody ConditionForActivity condition);

    @RequestMapping("quitAct")
    Proto<Boolean> quitActivity(@RequestBody ConditionForActivityUser condition);

    @RequestMapping("joinActivity")
    Proto<Boolean> joinActivity(@RequestBody ActivityUserDO activityUserDO);
}
