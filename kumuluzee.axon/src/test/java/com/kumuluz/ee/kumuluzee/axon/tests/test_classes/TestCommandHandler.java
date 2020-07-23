package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TestCommandHandler {

    @Inject
    private EventGateway eventGateway;

    public TestCommandHandler() {}

    @CommandHandler
    public void handle(TestCommand cmd) {
        if (cmd.getTestValue() <= 0)
            throw new IllegalArgumentException("test value <= 0");

        eventGateway.publish(new TestEvent(cmd.getId(), cmd.getTestValue()));
    }
}
