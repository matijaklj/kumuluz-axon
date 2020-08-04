package com.kumuluz.ee.axon.example.command;

import com.kumuluz.ee.axon.example.api.IssueCmd;
import com.kumuluz.ee.axon.example.api.IssuedEvt;
import com.kumuluz.ee.axon.example.api.RedeemCmd;
import com.kumuluz.ee.axon.example.api.RedeemedEvt;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

public class GiftCard {

    private final static Logger log = LoggerFactory.getLogger(GiftCard.class);

    @AggregateIdentifier
    private String id;
    private int remainingValue;

    public GiftCard() {
        log.debug("empty constructor invoked");
    }

    @CommandHandler
    public GiftCard(IssueCmd cmd) {
        log.info("handling {}", cmd);
        if (cmd.getAmount() <= 0) throw new IllegalArgumentException("amount <= 0");
        apply(new IssuedEvt(cmd.getId(), cmd.getAmount()));
    }

    @CommandHandler
    public void handle(RedeemCmd cmd) {
        if (cmd.getAmount() <= 0) {
            throw new IllegalArgumentException("amount <= 0");
        }
        if (cmd.getAmount() > remainingValue) {
            throw new IllegalStateException("amount > remaining value");
        }

        remainingValue -= cmd.getAmount();

        apply(new RedeemedEvt(id, cmd.getAmount()));
    }

    @EventHandler
    void createGiftCard(IssuedEvt evt) {
        log.info("applying {}", evt);
        id = evt.getId();
        remainingValue = evt.getAmount();
        log.debug("new remaining value: {}", remainingValue);
    }
}
