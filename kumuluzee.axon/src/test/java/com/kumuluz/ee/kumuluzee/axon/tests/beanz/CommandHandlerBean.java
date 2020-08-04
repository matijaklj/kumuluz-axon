package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestCommand;
import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Test Command handler bean
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
@ApplicationScoped
public class CommandHandlerBean {

    @Inject
    private EventGateway eventGateway;

    public CommandHandlerBean() {}

    @CommandHandler
    public void handle(TestCommand cmd) {
        if (cmd.getTestValue() <= 0)
            throw new IllegalArgumentException("test value <= 0");

        eventGateway.publish(new TestEvent(cmd.getId(), cmd.getTestValue()));
    }
}
