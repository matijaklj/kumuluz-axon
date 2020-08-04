package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestAggregate;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.config.Configuration;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.modelling.command.Repository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Test Aggregate repository bean
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class AggregateRepoBean {

    @Produces
    @ApplicationScoped
    public Repository<TestAggregate> testAggregateRepository(Configuration configuration) {
        return CachingEventSourcingRepository.builder(TestAggregate.class)
                .eventStore(configuration.eventStore())
                .cache(new WeakReferenceCache())
                .build();
    }
}
