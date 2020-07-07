package com.kumuluz.ee.samples.jpa;

import com.kumuluz.ee.samples.jpa.api.FindGiftCardQry;
import com.kumuluz.ee.samples.jpa.api.GiftCardRecord;
import com.kumuluz.ee.samples.jpa.api.IssueCmd;
import com.kumuluz.ee.samples.jpa.command.GiftCard;
import com.kumuluz.ee.samples.kumuluzee.axon.AggregateRepository;
import com.kumuluz.ee.samples.kumuluzee.axon.AxonConfiguration;
import com.kumuluz.ee.samples.kumuluzee.axon.AxonConfigurationCdiExtension;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.*;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.disruptor.commandhandling.DisruptorCommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.modelling.command.GenericJpaRepository;
import org.axonframework.modelling.command.Repository;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import com.kumuluz.ee.samples.jpa.query.GiftCardEventHandler;
import com.kumuluz.ee.samples.jpa.query.GiftCardQueryHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ApplicationScoped
public class AxonConfig {

    @Inject
    private TestProperty testProperty;

    @AxonConfiguration
    public Configurer testConfig(Configurer configurer) {
        DB querySideDB = DBMaker.memoryDB().make();
        ConcurrentMap querySideDBMap = querySideDB.hashMap("querySideDBMap").createOrOpen();

        configurer.configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine());

        //configurer.configureAggregate(GiftCard.class);



        configurer.eventProcessing(ep -> ep.registerEventHandler(
                c -> new GiftCardEventHandler(c.queryUpdateEmitter(), querySideDBMap))
        );

        configurer.registerQueryHandler(c -> new GiftCardQueryHandler(querySideDBMap));

        configurer.configureCommandBus(c ->

                DisruptorCommandBus.builder()
                        //.transactionManager(c.getComponent(NoTransactionManager.class))
                        //.messageMonitor(c.messageMonitor(SimpleCommandBus.class, "commandBus"))
                        .build()
        );
        EntityManagerProvider entityManagerProvider = new EntityManagerProvider() {
            @Override
            public EntityManager getEntityManager() {
                return null;
            }
        };

        configurer.registerComponent(Repository.class, c ->
                GenericJpaRepository.builder(GiftCard.class)
                        .entityManagerProvider(entityManagerProvider)
                        .eventBus(c.eventBus()).build()
        );


        configurer.configureAggregate(
                AggregateConfigurer.defaultConfiguration(GiftCard.class)
                .configureRepository(c -> c.getComponent(Repository.class))
        );

        //configurer.registerComponent(Repository.class, c -> EventSourcingRepository.builder(GiftCard.class).build());

        //config.commandGateway()


        return configurer;
        //CommandGateway cmdGateway = DefaultCommandGateway.builder().commandBus(config.commandBus()).build();
    }


}
