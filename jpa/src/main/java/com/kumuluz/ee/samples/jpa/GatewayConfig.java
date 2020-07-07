package com.kumuluz.ee.samples.jpa;

import com.kumuluz.ee.samples.jpa.command.GiftCard;
import com.kumuluz.ee.samples.kumuluzee.axon.AggregateRepository;
import com.kumuluz.ee.samples.kumuluzee.axon.AxonConfiguration;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactory;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler;
import org.axonframework.config.Configuration;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.modelling.command.Repository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ApplicationScoped
public class GatewayConfig {

    @AxonConfiguration
    @Named("myCommandGateway")
    public CommandGateway myCommandGateway(Configuration config) {
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        CommandGatewayFactory factory = CommandGatewayFactory.builder()
                .commandBus(config.commandBus())
                .retryScheduler(
                        IntervalRetryScheduler.builder().retryExecutor(scheduler).retryInterval(11).build())
                .build();
        // note that the commandBus can be obtained from the Configuration
        // object returned on `configurer.initialize()`.
        MyCommandGateway myGateway = factory.createGateway(MyCommandGateway.class);

        CommandGateway commandGateway = DefaultCommandGateway.builder()
                .commandBus(config.commandBus())
                //.dispatchInterceptors()
                //.retryScheduler(...)
                .build();

        return commandGateway;
    }

    @AxonConfiguration
    @Named("defaultCommandGateway")
    public CommandGateway getDefCommandGateway(Configuration config) {
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        return DefaultCommandGateway.builder().commandBus(config.commandBus())
                .retryScheduler(
                        IntervalRetryScheduler.builder().retryExecutor(scheduler).retryInterval(666).build()).build();
    }

    @AggregateRepository
    public Repository<GiftCard> myProductRepository(Configuration configuration) {
        return EventSourcingRepository
                .builder(GiftCard.class)
                .eventStore(configuration.eventStore())
                .build();
    }
}

