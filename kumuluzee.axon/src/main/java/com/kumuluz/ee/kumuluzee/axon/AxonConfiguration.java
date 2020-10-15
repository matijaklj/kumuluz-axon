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
 * Axon configuration class.
 *
 * @author Matija Kljun
 * @since 1.0.0
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

    public void afterPropertiesSet() {
        config = configurer.buildConfiguration();
    }

}

