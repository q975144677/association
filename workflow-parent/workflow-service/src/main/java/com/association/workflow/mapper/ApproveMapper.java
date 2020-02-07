package com.association.workflow.mapper;

import com.association.workflow.condition.ConditionForApprove;
import com.association.workflow.model.ApproveDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApproveMapper {
    Boolean createNewApprove(@Param("condition") ApproveDO approveDO);

    Boolean updateApprove(@Param("condition")ApproveDO approveDO );

    Boolean deleteApprove(@Param("guid")String guid);

    List<ApproveDO> queryApproveList(@Param("condition")ConditionForApprove condition);
    Long countApprove(@Param("condition")ConditionForApprove condition);
    ApproveDO getApprove(@Param("guid")String guid);
}
