package com.kumuluz.ee.axon.example;

import com.kumuluz.ee.axon.example.command.GiftCard;
import com.kumuluz.ee.axon.example.api.GiftCardRecord;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

@ApplicationScoped
public class AxonConfig {

    private static final Logger log = Logger.getLogger(AxonConfig.class.getName());

    @Produces
    public ConcurrentMap<String, GiftCardRecord> querySideMap() {
        DB querySideDB = DBMaker.memoryDB().make();
        return (ConcurrentMap<String, GiftCardRecord>) querySideDB.hashMap("querySideDBMap").createOrOpen();
    }

    @Produces
    @ApplicationScoped
    public Configurer testConfig(ConcurrentMap<String, GiftCardRecord> querySideDBMap) {
        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        configurer.configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine());

        configurer.configureAggregate(GiftCard.class);

        /* TESTING EVENT PROCESSING
        configurer.eventProcessing(ep -> ep.registerEventHandler(
                c -> new GiftCardEventHandler(c.queryUpdateEmitter(), querySideDBMap))
        );

         */

        //configurer.registerQueryHandler(c -> new GiftCardQueryHandler(querySideDBMap));

        return configurer;
    }
}
