package com.kumuluz.ee.samples.jpa.command;

import com.kumuluz.ee.samples.jpa.api.IssueCmd;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class GiftCardCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @CommandHandler
    public void handle(IssueCmd cmd) {
        log.info("Issue Giftcard Command id {}, val {}", cmd.getId(), cmd.getAmount());
    }
    // omitted constructor
}
