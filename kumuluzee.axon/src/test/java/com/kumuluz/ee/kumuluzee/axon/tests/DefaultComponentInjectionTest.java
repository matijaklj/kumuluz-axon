package com.kumuluz.ee.kumuluzee.axon.tests;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.serialization.Serializer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Axon default components injection tests.
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class DefaultComponentInjectionTest  extends Arquillian {


    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Configuration configuration;
    @Inject
    private CommandGateway commandGateway;
    @Inject
    private CommandBus commandBus;
    @Inject
    private EventBus eventBus;
    @Inject
    private EventGateway eventGateway;
    @Inject
    private QueryBus queryBus;
    @Inject
    private QueryGateway queryGateway;
    @Inject
    private Serializer serializer;
    @Inject
    @Named("eventSerializer")
    private Serializer eventSerializer;
    @Inject
    @Named("messageSerializer")
    private Serializer messageSerializer;

    @Test
    public void testAggregateIsInjectable() {
        Assert.assertNotNull(configuration);
        Assert.assertNotNull(commandGateway);
        Assert.assertNotNull(commandBus);
        Assert.assertNotNull(eventBus);
        Assert.assertNotNull(eventGateway);
        Assert.assertNotNull(queryBus);
        Assert.assertNotNull(queryGateway);
        Assert.assertNotNull(serializer);
        Assert.assertNotNull(eventSerializer);
        Assert.assertNotNull(messageSerializer);
    }
}
