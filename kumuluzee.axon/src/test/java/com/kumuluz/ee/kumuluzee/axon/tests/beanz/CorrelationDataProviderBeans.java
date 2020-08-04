package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.axonframework.messaging.correlation.MessageOriginProvider;
import org.axonframework.messaging.correlation.SimpleCorrelationDataProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Test Correlation data provider bean
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class CorrelationDataProviderBeans {

    @Produces
    @ApplicationScoped
    public CorrelationDataProvider messageOriginProvider() {
        return new MessageOriginProvider();
    }

    @Produces
    @ApplicationScoped
    public CorrelationDataProvider SimpleCorrelationDataProvider() {
        return new SimpleCorrelationDataProvider();
    }
}
