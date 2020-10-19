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

package com.kumuluz.ee.axon.example;

import com.kumuluz.ee.axon.example.command.GiftCard;
import com.kumuluz.ee.axon.example.api.queries.GiftCardRecord;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.config.*;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.modelling.command.Repository;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Axon configuration.
 *
 * @author Matija Kljun
 */
@ApplicationScoped
public class AxonConfig implements Serializable {

    private static final Logger log = Logger.getLogger(AxonConfig.class.getName());

    @Produces
    @ApplicationScoped
    public ConcurrentMap<String, GiftCardRecord> querySideMap() {
        DB querySideDB = DBMaker.memoryDB().make();
        return (ConcurrentMap<String, GiftCardRecord>) querySideDB.hashMap("querySideDBMap").createOrOpen();
    }

    @Produces
    @ApplicationScoped
    public Repository<GiftCard> giftCardRepository(Configuration configuration, Cache cache) {
        return EventSourcingRepository.builder(GiftCard.class)
                .cache(cache)
                .eventStore(configuration.eventStore())
                .build();
    }

    @Produces
    @ApplicationScoped
    public Cache cache() {
        return new WeakReferenceCache();
    }

    /*
    @Produces
    @ApplicationScoped
    public ModuleConfiguration myAggregateConfigurer() {
        AggregateConfigurer agg = AggregateConfigurer.defaultConfiguration(GiftCard.class);

        agg.aggregateType();

        return agg;
    }

     */

    /*@Produces
    @ApplicationScoped
    public Configurer testConfig() {
        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        configurer.configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine());
        //configurer.configureAggregate(GiftCard.class);
        configurer.eventProcessing().registerTokenStore(conf -> new InMemoryTokenStore());

        return configurer;
    }

     */

/*
    @Produces
    @ApplicationScoped
    public TransactionManager transactionManager() {
        return new JtaTransactionManager();
        // return NoTransactionManager.INSTANCE;
    }

    @Produces
    @ApplicationScoped
    public EntityManagerProvider entityManagerProvider() {
        return new TestEntityManagerProvider();
    }

    @Produces
    @ApplicationScoped
    public EntityManager entityManager() {
        try {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
            return factory.createEntityManager();
            //return (EntityManager) new InitialContext().lookup("java:comp/env/persistence/test");
        } catch (Exception ex) {
            System.out.println("Failed to look up entity manager. " + ex.getMessage());
        }

        return null;
    }

    @Produces
    @ApplicationScoped
    public EntityManagerProvider entityManagerProvider(
            EntityManager entityManager) {
        return new SimpleEntityManagerProvider(entityManager);
    }
 */
}
