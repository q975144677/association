package com.association.workflow.strategy;

import com.association.workflow.enumerations.Messages;
import org.springframework.stereotype.Component;

@Component(Messages.SEND_MESSAGE)
public class SendMessageStrategy  extends BaseMessageStrategy implements BaseStrategy {
    @Override
    public Boolean invoke() {
        return null;
    }
}
