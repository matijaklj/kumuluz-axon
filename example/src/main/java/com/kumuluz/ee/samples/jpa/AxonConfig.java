package com.kumuluz.ee.samples.jpa;

import com.kumuluz.ee.samples.jpa.command.GiftCard;
import com.kumuluz.ee.samples.jpa.command.GiftCardCommandHandler;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.disruptor.commandhandling.DisruptorCommandBus;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.modelling.command.GenericJpaRepository;
import org.axonframework.modelling.command.Repository;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import com.kumuluz.ee.samples.jpa.query.GiftCardEventHandler;
import com.kumuluz.ee.samples.jpa.query.GiftCardQueryHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class AxonConfig {

    @Inject
    private TestProperty testProperty;

    @Produces
    @ApplicationScoped
    public Configurer testConfig() {
        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        DB querySideDB = DBMaker.memoryDB().make();
        ConcurrentMap querySideDBMap = querySideDB.hashMap("querySideDBMap").createOrOpen();

        configurer.configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine());

        configurer.configureAggregate(GiftCard.class);

        configurer.registerCommandHandler(c -> new GiftCardCommandHandler() );

        configurer.eventProcessing(ep -> ep.registerEventHandler(
                c -> new GiftCardEventHandler(c.queryUpdateEmitter(), querySideDBMap))
        );

        configurer.registerQueryHandler(c -> new GiftCardQueryHandler(querySideDBMap));


        return configurer;
    }
}
