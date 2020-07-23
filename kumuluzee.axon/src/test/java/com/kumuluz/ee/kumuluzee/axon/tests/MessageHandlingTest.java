package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.tests.beanz.ValueChanger;
import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.GenericQueryMessage;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

public class MessageHandlingTest extends Arquillian {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(TestEventHandler.class)
                .addClass(TestQueryHandler.class)
                .addClass(TestCommandHandler.class)
                .addClass(TestEvent.class)
                .addClass(TestCommand.class)
                .addClass(TestQuery.class)
                .addClass(ValueChanger.class)
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private EventGateway eventGateway;

    @Inject
    private QueryBus queryBus;

    @Inject
    private CommandGateway commandGateway;

    @Inject
    private ValueChanger valueChanger;

    @Test
    public void testEventHandling() {
        valueChanger.setValue(0);
        Assert.assertEquals(valueChanger.getValue(), 0);

        for (int i = 1; i < 10; i++) {
            eventGateway.publish(new TestEvent("test-event", i));

            Assert.assertEquals(valueChanger.getValue(), i);
        }
    }

    @Test
    public void testQueryHandling() {
        int val = 2;
        GenericQueryMessage<Integer, Integer> query =
                new GenericQueryMessage<>(val, ResponseTypes.instanceOf(Integer.class));
        // 2. send a query message and print query response
        queryBus.query(query).thenAccept(res ->
        {
            Assert.assertEquals((int)res.getPayload(), val*2);
        });
    }

    @Test
    public void testCommandHandling() {
        Assert.assertThrows(() -> this.commandGateway.send(new TestCommand("testCommandHandling", -11)).get());

        int val = 100;
        TestCommand cmd = new TestCommand("testCommandHandling", val);
        CompletableFuture<String> futureResult = this.commandGateway
                .send(cmd);

        try {
            futureResult.get();
            Assert.assertEquals(valueChanger.getValue(), val);
        } catch (Exception e) {
            Assert.fail();
        }

    }
}
