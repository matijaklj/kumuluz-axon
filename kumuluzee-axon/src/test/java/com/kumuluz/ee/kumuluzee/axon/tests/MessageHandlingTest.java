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

package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.tests.beanz.CommandHandlerBean;
import com.kumuluz.ee.kumuluzee.axon.tests.beanz.EventHandlerBean;
import com.kumuluz.ee.kumuluzee.axon.tests.beanz.QueryHandlerBean;
import com.kumuluz.ee.kumuluzee.axon.tests.beanz.ValueChanger;
import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.GenericQueryMessage;
import org.axonframework.queryhandling.QueryBus;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * Axon message handling tests.
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
public class MessageHandlingTest extends Arquillian {

    private static final Logger log = Logger.getLogger(MessageHandlingTest.class.getName());

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(EventHandlerBean.class)
                .addClass(QueryHandlerBean.class)
                .addClass(CommandHandlerBean.class)
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

        for (int i = 0; i < 10; i++) {
            eventGateway.publish(new TestEvent("test-event", i));

            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }

            Assert.assertEquals(valueChanger.getValue(), i);
        }
    }

    @Test
    public void testQueryHandling() {
        int val = 2;
        GenericQueryMessage<Integer, Integer> query =
                new GenericQueryMessage<>(val, ResponseTypes.instanceOf(Integer.class));

        queryBus.query(query).thenAccept(res ->
            Assert.assertEquals((int)res.getPayload(), val*2)
        );
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
            log.warning(e.getMessage());
            Assert.fail();
        }

    }
}
