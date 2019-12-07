package com.association.workflow.strategy;

import com.association.workflow.enumerations.Messages;
import org.springframework.stereotype.Component;

@Component(Messages.PULL_MESSAGE)
public class PullMessageStrategy  extends BaseMessageStrategy implements BaseStrategy {
    @Override
    public Boolean invoke() {
        return null;
    }
}
