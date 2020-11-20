/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.kumuluz.ee.axon.example.command;

import com.kumuluz.ee.axon.example.api.commands.IssueCommand;
import com.kumuluz.ee.axon.example.api.events.IssuedEvent;
import com.kumuluz.ee.axon.example.api.commands.RedeemCommand;
import com.kumuluz.ee.axon.example.api.events.RedeemedEvent;
import com.kumuluz.ee.kumuluzee.axon.stereotype.Aggregate;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * Gift card aggregate.
 *
 * @author Matija Kljun
 */
@Aggregate
@ApplicationScoped
public class GiftCard {

    private final static Logger log = LoggerFactory.getLogger(GiftCard.class);

    @AggregateIdentifier
    private String id;
    private int remainingValue;

    @CommandHandler
    public GiftCard(IssueCommand cmd) {
        log.info("handling {}", cmd);
        if (cmd.getAmount() <= 0) throw new IllegalArgumentException("amount <= 0");
        apply(new IssuedEvent(cmd.getId(), cmd.getAmount()));
    }

    @CommandHandler
    public void handle(RedeemCommand cmd) {
        if (cmd.getAmount() <= 0) {
            throw new IllegalArgumentException("amount <= 0");
        }
        if (cmd.getAmount() > remainingValue) {
            throw new IllegalStateException("amount > remaining value");
        }

        apply(new RedeemedEvent(id, cmd.getAmount()));
    }

    @EventSourcingHandler
    void createGiftCard(IssuedEvent evt) {
        log.info("applying {}", evt);
        id = evt.getId();
        remainingValue = evt.getAmount();
        log.debug("new remaining value: {}", remainingValue);
    }

    @EventSourcingHandler
    public void on(RedeemedEvent evt) {
        log.debug("applying {}", evt);
        remainingValue -= evt.getAmount();
        log.debug("new remaining value: {}", remainingValue);
    }

    public GiftCard() {
        // Required by Axon
        log.debug("empty constructor invoked");
    }
}
