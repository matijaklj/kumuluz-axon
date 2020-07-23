package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.tests.beanz.ValueChanger;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.config.Configuration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class AxonServerConfigTest extends Arquillian {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsResource("config.yml")
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    Configuration configuration;

    @Test
    public void testAxonServerConfiguration() {
        AxonServerConfiguration serverConfiguration = this.configuration.getComponent(AxonServerConfiguration.class);

        Assert.assertNotNull(serverConfiguration);
        Assert.assertEquals(serverConfiguration.getClientId(), "testClientId");
        Assert.assertEquals(serverConfiguration.getComponentName(), "testComponentName");

        testFlowConfigurations(serverConfiguration.getDefaultFlowControlConfiguration(), 111);
        testFlowConfigurations(serverConfiguration.getEventFlowControl(), 222);
        testFlowConfigurations(serverConfiguration.getCommandFlowControl(), 333);
        testFlowConfigurations(serverConfiguration.getQueryFlowControl(), 444);

    }

    private void testFlowConfigurations(AxonServerConfiguration.FlowControlConfiguration conf, Integer expectedResult) {
        Assert.assertEquals(conf.getInitialNrOfPermits(), expectedResult);
        Assert.assertEquals(conf.getNrOfNewPermits(), expectedResult);
        Assert.assertEquals(conf.getNewPermitsThreshold(), expectedResult);
    }
}
