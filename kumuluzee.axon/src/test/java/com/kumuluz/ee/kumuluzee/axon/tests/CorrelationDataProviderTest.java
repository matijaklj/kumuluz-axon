package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.tests.beanz.CorrelationDataProviderBeans;
import org.axonframework.config.Configuration;
import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;

/**
 * Axon Correlation Data Provider test.
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class CorrelationDataProviderTest extends Arquillian {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(CorrelationDataProviderBeans.class)
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Configuration configuration;

    @Test
    public void injectAxonConfigTest() {
        Assert.assertNotNull(configuration, "Axon Configuration");

        List<CorrelationDataProvider> correlationDataProviders = this.configuration.correlationDataProviders();
        Assert.assertEquals(correlationDataProviders.size(), 2);

    }
}
