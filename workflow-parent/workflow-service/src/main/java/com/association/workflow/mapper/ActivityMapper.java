package com.association.workflow.mapper;

import com.association.workflow.condition.ConditionForActivity;
import com.association.workflow.model.ActivityDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityMapper {
    Boolean createNewActivity(@Param("condition")ActivityDO activityDO);

    Boolean updateActivity(@Param("condition")ActivityDO activityDO);

    Boolean deleteActivity(@Param("guid")String guid);

    List<ActivityDO> queryActivity(@Param("condition")ConditionForActivity condition);

}
