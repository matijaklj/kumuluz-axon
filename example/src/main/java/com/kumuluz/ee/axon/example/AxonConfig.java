package com.kumuluz.ee.axon.example;

import com.kumuluz.ee.axon.example.command.GiftCard;
import com.kumuluz.ee.axon.example.api.GiftCardRecord;
import com.kumuluz.ee.axon.example.query.GiftCardEventHandler;
import com.kumuluz.ee.kumuluzee.axon.transaction.JtaTransactionManager;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.Transaction;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

@ApplicationScoped
public class AxonConfig implements Serializable {

    private static final Logger log = Logger.getLogger(AxonConfig.class.getName());

    @Produces
    public ConcurrentMap<String, GiftCardRecord> querySideMap() {
        DB querySideDB = DBMaker.memoryDB().make();
        return (ConcurrentMap<String, GiftCardRecord>) querySideDB.hashMap("querySideDBMap").createOrOpen();
    }

    @Produces
    @ApplicationScoped
    public Configurer testConfig(EntityManagerProvider entityManagerProvider) {
        /*Configurer configurer = DefaultConfigurer.defaultConfiguration();

        configurer.configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine());

        configurer.configureAggregate(GiftCard.class);

        return configurer;*/

        TransactionManager transactionManager = new JtaTransactionManager();

        Configurer configurer =  DefaultConfigurer.jpaConfiguration(entityManagerProvider, transactionManager);

        configurer.configureAggregate(GiftCard.class);

        configurer.eventProcessing().registerTokenStore(conf -> new InMemoryTokenStore());

        return configurer;
    }
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

 */

    /**
     * Produces the entity manager.
     *
     * @return entity manager.
     */
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

    /**
     * Produces the entity manager provider.
     *
     * @return entity manager provider.
     */
    @Produces
    @ApplicationScoped
    public EntityManagerProvider entityManagerProvider(
            EntityManager entityManager) {
        return new SimpleEntityManagerProvider(entityManager);
    }
}
