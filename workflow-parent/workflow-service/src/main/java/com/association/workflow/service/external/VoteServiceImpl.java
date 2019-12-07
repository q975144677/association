package com.association.workflow.service.external;

import com.association.workflow.condition.ConditionForVote;
import com.association.workflow.iface.VoteIface;
import component.Proto;

public class VoteServiceImpl implements VoteIface {
    @Override
    public Proto<Boolean> vote(ConditionForVote condition) {
            return null;
    }

    @Override
    public Proto<Boolean> isVoted(ConditionForVote condition) {
        return null;
    }
}
