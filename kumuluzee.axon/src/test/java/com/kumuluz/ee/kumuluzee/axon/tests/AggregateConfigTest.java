package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestAggregate;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.config.Configuration;
import org.axonframework.modelling.command.Repository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

public class AggregateConfigTest  extends Arquillian {


    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(TestAggregate.class)
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Configuration configuration;

    @Test
    public void testAggregateIsInjectable() {
        Assert.assertThrows(() -> CDI.current().select(TestAggregate.class).get());
    }

    @Test
    public void testConfiguredAggregate() {
        Repository<TestAggregate> repo = configuration.repository(TestAggregate.class);

        Assert.assertNotNull(repo, "Test aggregate repository shouldn't be null.");
    }
}
