package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForActivity;
import com.association.workflow.model.ActivityDO;
import component.PaginProto;
import component.Proto;

import java.util.List;

public interface ActivityIface {
    /**
     * 新增活动(待审核)
     */
    Proto<Boolean> createNewActivity(ActivityDO activity);

    /**
     * 更新活动
     */
    Proto<Boolean> updateActivity(ActivityDO activity);

    /**
     * 查询活动
     */
    PaginProto<List<ActivityDO>> queryActivity(ConditionForActivity condition);
}
