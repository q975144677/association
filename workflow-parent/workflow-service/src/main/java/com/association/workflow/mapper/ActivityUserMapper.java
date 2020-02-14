package com.association.workflow.mapper;

import com.association.workflow.condition.ConditionForActivityUser;
import com.association.workflow.model.ActivityUserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityUserMapper {
    Boolean save(@Param("condition") ActivityUserDO activityUserDO);
    Boolean delete(@Param("guid") String guid);
    List<ActivityUserDO> findActivityUser(@Param("condition") ConditionForActivityUser condition);
    Boolean deleteRef(@Param("activityGuid") String actGuid , @Param("userGuid") String userGuid) ;
}
