package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.tests.beanz.SerializersBean;
import org.axonframework.config.Configuration;
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
 * Axon Serializers tests.
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class SerializersTest extends Arquillian {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(SerializersBean.class)
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Configuration configuration;
    @Inject
    @Named("serializer")
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
        Assert.assertNotNull(serializer);
        Assert.assertNotNull(eventSerializer);
        Assert.assertNotNull(messageSerializer);

        Assert.assertEquals(configuration.serializer(), serializer);
        Assert.assertEquals(configuration.eventSerializer(), eventSerializer);
        Assert.assertEquals(configuration.messageSerializer(), messageSerializer);
    }
}
