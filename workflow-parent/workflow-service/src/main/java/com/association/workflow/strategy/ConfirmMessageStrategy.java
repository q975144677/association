package com.association.workflow.strategy;

import com.association.workflow.enumerations.Messages;
import org.springframework.stereotype.Component;

@Component(Messages.CONFIRM_MESSAGE)
public class ConfirmMessageStrategy extends BaseMessageStrategy implements BaseStrategy {
    @Override
    public Boolean invoke() {
        return null;
    }
}
