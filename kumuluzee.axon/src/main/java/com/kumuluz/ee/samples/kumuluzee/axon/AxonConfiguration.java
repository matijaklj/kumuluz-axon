package com.kumuluz.ee.samples.kumuluzee.axon;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.EventProcessingConfiguration;
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
import org.axonframework.spring.config.NoBeanOfType;

import java.util.List;
import java.util.function.Supplier;

public class AxonConfiguration implements Configuration {
    private final Configurer configurer;
    private Configuration config;
    private volatile boolean running = false;

    public AxonConfiguration(Configurer configurer) {
        this.configurer = configurer;
        this.config = this.configurer.buildConfiguration();
    }

    public CommandBus commandBus() {
        return this.config.commandBus();
    }

    public QueryBus queryBus() {
        return this.config.queryBus();
    }

    public EventBus eventBus() {
        return this.config.eventBus();
    }

    public QueryUpdateEmitter queryUpdateEmitter() {
        return this.config.queryUpdateEmitter();
    }

    /*@NoBeanOfType(QueryBus.class)
    @Bean({"queryBus"})
    public QueryBus defaultQueryBus() {
        return this.config.queryBus();
    }

    @NoBeanOfType(QueryUpdateEmitter.class)
    @Bean({"queryUpdateEmitter"})
    public QueryUpdateEmitter defaultQueryUpdateEmitter() {
        return this.config.queryUpdateEmitter();
    }

    @NoBeanOfType(CommandBus.class)
    @Bean({"commandBus"})
    public CommandBus defaultCommandBus() {
        return this.commandBus();
    }

    @NoBeanOfType(EventBus.class)
    @Bean({"eventBus"})
    public EventBus defaultEventBus() {
        return this.eventBus();
    }
     */

    public ResourceInjector resourceInjector() {
        return this.config.resourceInjector();
    }

    public EventProcessingConfiguration eventProcessingConfiguration() {
        return this.config.eventProcessingConfiguration();
    }

    /*
    @NoBeanOfType(CommandGateway.class)
    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return DefaultCommandGateway.builder().commandBus(commandBus).build();
    }

    @NoBeanOfType(QueryGateway.class)
    @Bean
    public QueryGateway queryGateway(QueryBus queryBus) {
        return DefaultQueryGateway.builder().queryBus(queryBus).build();
    }
     */

    public <T> Repository<T> repository(Class<T> aggregateType) {
        return this.config.repository(aggregateType);
    }

    public <T> T getComponent(Class<T> componentType, Supplier<T> defaultImpl) {
        return this.config.getComponent(componentType, defaultImpl);
    }

    public <M extends Message<?>> MessageMonitor<? super M> messageMonitor(Class<?> componentType, String componentName) {
        return this.config.messageMonitor(componentType, componentName);
    }

    public Serializer eventSerializer() {
        return this.config.eventSerializer();
    }

    public Serializer messageSerializer() {
        return this.config.messageSerializer();
    }

    public List<CorrelationDataProvider> correlationDataProviders() {
        return this.config.correlationDataProviders();
    }

    public HandlerDefinition handlerDefinition(Class<?> inspectedType) {
        return this.config.handlerDefinition(inspectedType);
    }

    public EventUpcasterChain upcasterChain() {
        return this.config.upcasterChain();
    }

    public List<ModuleConfiguration> getModules() {
        return this.config.getModules();
    }

    public void onStart(int phase, Runnable startHandler) {
        this.config.onStart(phase, startHandler);
    }

    public void onShutdown(int phase, Runnable shutdownHandler) {
        this.config.onShutdown(phase, shutdownHandler);
    }

    public void start() {
        this.config.start();
        this.running = true;
    }

    public void shutdown() {
        this.config.shutdown();
    }

    public void stop() {
        this.shutdown();
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean isAutoStartup() {
        return true;
    }

    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    public int getPhase() {
        return 0;
    }

    public void afterPropertiesSet() {
        this.config = this.configurer.buildConfiguration();
    }

}