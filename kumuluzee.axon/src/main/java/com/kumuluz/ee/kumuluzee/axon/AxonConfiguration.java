package com.kumuluz.ee.kumuluzee.axon;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.config.LifecycleHandler;
import org.axonframework.config.ModuleConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.annotation.HandlerDefinition;
import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.axonframework.modelling.command.Repository;
import org.axonframework.modelling.saga.ResourceInjector;
import org.axonframework.monitoring.MessageMonitor;
import org.axonframework.queryhandling.DefaultQueryGateway;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.upcasting.event.EventUpcasterChain;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.function.Supplier;

/**
 * Axon Configuration class
 */
public class AxonConfiguration implements Configuration {

    private final Configurer configurer;
    private Configuration config;
    private volatile boolean running = false;

    /**
     * Initializes a new AxonConfiguration that uses the given {@code configurer} to build the configuration.
     *
     * @param configurer Configuration builder for the AxonConfiguration.
     */
    public AxonConfiguration(Configurer configurer) {
        this.configurer = configurer;
    }

    @Override
    public CommandBus commandBus() {
        return config.commandBus();
    }

    @Override
    public CommandGateway commandGateway() {
        return config.commandGateway();
    }

    @Override
    public QueryBus queryBus() {
        return config.queryBus();
    }

    @Override
    public EventBus eventBus() {
        return config.eventBus();
    }

    @Override
    public QueryUpdateEmitter queryUpdateEmitter() {
        return config.queryUpdateEmitter();
    }

    @Override
    public ResourceInjector resourceInjector() {
        return config.resourceInjector();
    }

    @Override
    public EventProcessingConfiguration eventProcessingConfiguration() {
        return config.eventProcessingConfiguration();
    }

    @Override
    public <T> Repository<T> repository(Class<T> aggregateType) {
        return config.repository(aggregateType);
    }

    @Override
    public <T> T getComponent(Class<T> componentType, Supplier<T> defaultImpl) {
        return config.getComponent(componentType, defaultImpl);
    }

    @Override
    public <M extends Message<?>> MessageMonitor<? super M> messageMonitor(Class<?> componentType, String componentName) {
        return config.messageMonitor(componentType, componentName);
    }

    @Override
    public Serializer eventSerializer() {
        return config.eventSerializer();
    }

    @Override
    public Serializer messageSerializer() {
        return config.messageSerializer();
    }

    @Override
    public List<CorrelationDataProvider> correlationDataProviders() {
        return config.correlationDataProviders();
    }

    @Override
    public HandlerDefinition handlerDefinition(Class<?> inspectedType) {
        return config.handlerDefinition(inspectedType);
    }

    @Override
    public EventUpcasterChain upcasterChain() {
        return config.upcasterChain();
    }

    @Override
    public List<ModuleConfiguration> getModules() {
        return config.getModules();
    }

    @Override
    public void onStart(int phase, LifecycleHandler startHandler) {
        config.onStart(phase, startHandler);
    }

    @Override
    public void onShutdown(int phase, LifecycleHandler shutdownHandler) {
        config.onShutdown(phase, shutdownHandler);
    }

    @Override
    public void start() {
        this.afterPropertiesSet();
        config.start();
        this.running = true;
    }

    @Override
    public void shutdown() {
        config.shutdown();
    }

    @PostConstruct
    public void afterPropertiesSet() {
        // todo how to do this ?
        config = configurer.buildConfiguration();
    }

}

