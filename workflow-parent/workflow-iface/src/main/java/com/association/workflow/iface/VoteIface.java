package com.association.workflow.iface;

import com.association.workflow.condition.ConditionForVote;
import component.Proto;

public interface VoteIface {

    /**
     * 点赞操作,再次操作即取消点赞（redis）
     */
    Proto<Boolean> vote(ConditionForVote condition);

    /**
     * 是否点过赞
     */
    Proto<Boolean> isVoted(ConditionForVote condition);

}
