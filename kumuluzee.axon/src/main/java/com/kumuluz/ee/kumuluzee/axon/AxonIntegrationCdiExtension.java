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

import com.kumuluz.ee.kumuluzee.axon.stereotype.Aggregate;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.lock.LockFactory;
import org.axonframework.common.lock.NullLockFactory;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.*;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.eventhandling.ErrorHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.axonframework.modelling.command.GenericJpaRepository;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.upcasting.event.EventUpcaster;
import org.jboss.weld.literal.NamedLiteral;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import javax.inject.Named;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * CDI Extension for configuring Axon framework.
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
public class AxonIntegrationCdiExtension implements Extension {

    private static final Logger log = Logger.getLogger(AxonIntegrationCdiExtension.class.getName());

    private Producer<Configurer> configurerProducer;
    private Bean<?> configurerBean;
    private Bean<?> commandBusProducerBean;
    private Bean<?> commandGatewayProducerBean;
    private Bean<?> queryBusProducerBean;
    private Bean<?> queryGatewayProducerBean;
    private Bean<?> eventBusProducerBean;
    private Bean<?> eventGatewayProducerBean;
    private Bean<?> eventProcessingModuleProducerBean;

    private Bean<?> serializerProducerBean; // todo 2 of them
    private Bean<?> eventSerializerProducerBean; // todo 2 of them
    private Bean<?> messageSerializerProducerBean; // todo 2 of them

    private Bean<?> eventStorageEngineBean;
    private Bean<?> entityManagerProviderBean;
    private Bean<?> transactionManagerBean;
    private Bean<?> tokenStoreBean;
    private Bean<?> listenerInvocationErrorHandlerBean;
    private Bean<?> errorHandlerBean;
    private Bean<?> queryUpdateEmitterBean;
    private Bean<?> deadlineManagerBean;

    private final List<Bean<?>> correlationDataProviderBeans = new ArrayList<>();
    private final List<Bean<?>> eventUpcasterBeans = new ArrayList<>();

    private final List<AggregateDefinition> aggregates = new ArrayList<>();
    private final List<MessageHandlingBeanDefinition> messageHandlers = new ArrayList<>();

    private final Map<String, Bean<?>> aggregateRepositoryBeansMap = new HashMap<>();

    <T> void processMessageHandlingBean(@Observes final ProcessBean<T> processBean) {
        MessageHandlingBeanDefinition.inspect(processBean.getBean(), processBean.getAnnotated())
                .ifPresent(bean -> {
                    log.fine("Found Message Handler bean " + bean);
                    messageHandlers.add(bean);
                });
    }

    <T> void processAggregate(@Observes @WithAnnotations({Aggregate.class})
                              final ProcessAnnotatedType<T> processAnnotatedType) {
        // TODO Aggregate classes may need to be vetoed so that CDI does not
        // actually try to manage them.

        final Class<?> clazz = processAnnotatedType.getAnnotatedType().getJavaClass();

        log.fine("Found aggregate: " + clazz + ".");

        processAnnotatedType.veto();

        aggregates.add(new AggregateDefinition(clazz));
    }

    // todo try doing beanManager lookups for this after afterBeanDiscovery or later
    <T,X> void processAggregateRepositoryProducer(
            @Observes final ProcessProducerMethod<Repository<X>, T> processProducer) {
        log.fine("Found producer for repository: " + processProducer.getAnnotatedProducerMethod().getJavaMember().getName());

        String repositoryName = processProducer.getAnnotatedProducerMethod().getJavaMember().getName();

        this.aggregateRepositoryBeansMap.put(repositoryName, processProducer.getBean());
    }

    <T> void processConfigurerProducer(
            @Observes final ProcessProducer<T, Configurer> processProducer, BeanManager bm) {

        log.fine("Producer for Configurer found: " + processProducer.getProducer());

        if (this.configurerProducer != null) {
            log.severe("There can be only one Configurer producer!");
        }
        this.configurerProducer = processProducer.getProducer();

        // todo how to veto the producer of configurer
    }

    <T> void processCommandBusProducerMethod(
            @Observes final ProcessProducerMethod<CommandBus, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for CommandBus found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.commandBusProducerBean != null) {
            log.severe("There can be only one CommandBus producer!");
        }
        this.commandBusProducerBean = ppm.getBean();
    }

    <T> void processCommandGatewayProducerMethod(
            @Observes final ProcessProducerMethod<CommandGateway, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for CommandGateway found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.commandGatewayProducerBean != null) {
            log.severe("There can be only one CommandGateway producer!");
        }
        this.commandGatewayProducerBean = ppm.getBean();
    }

    <T> void processQueryBusProducerMethod(
            @Observes final ProcessProducerMethod<QueryBus, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for QueryBus found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.queryBusProducerBean != null) {
            log.severe("There can be only one QueryBus producer!");
        }
        this.queryBusProducerBean = ppm.getBean();
    }

    <T> void processQueryGatewayProducerMethod(
            @Observes final ProcessProducerMethod<QueryGateway, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for QueryGateway found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.queryGatewayProducerBean != null) {
            log.severe("There can be only one QueryGateway producer!");
        }
        this.queryGatewayProducerBean = ppm.getBean();
    }

    <T> void processEventBusProducerMethod(
            @Observes final ProcessProducerMethod<EventBus, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for EventBus found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.eventBusProducerBean != null) {
            log.severe("There can be only one EventBus producer!");
        }
        this.eventBusProducerBean = ppm.getBean();
    }

    <T> void processEventGatewayProducerMethod(
            @Observes final ProcessProducerMethod<EventGateway, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for EventGateway found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.eventGatewayProducerBean != null) {
            log.severe("There can be only one EventGateway producer!");
        }
        this.eventGatewayProducerBean = ppm.getBean();
    }

    // todo remove test
    public void processSerializerBeanAttributes(@Observes final ProcessBeanAttributes<Serializer> event) {
        BeanAttributes<Serializer> ba = event.getBeanAttributes();

    }

    <T> void processSerializerProducerMethod(
            @Observes final ProcessProducerMethod<Serializer, T> ppm) {

        if (ppm.getAnnotated().isAnnotationPresent(Named.class)) {
            String name = ppm.getAnnotated().getAnnotation(Named.class).value();

            if (name.equals("eventSerializer")) {
                log.fine("Producer method for Event Serializer (named 'eventSerializer') found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

                eventSerializerProducerBean = ppm.getBean();
            } else if (name.equals("messageSerializer")) {
                log.fine("Producer method for Message Serializer (named 'messageSerializer') found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

                messageSerializerProducerBean = ppm.getBean();
            } else if (name.equals("serializer")) {
                log.fine("Producer method for General Serializer (named 'serializer') found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

                if (this.serializerProducerBean != null) {
                    log.severe("There can be only one General Serializer producer!");
                }
                this.serializerProducerBean = ppm.getBean();
            }
        } else {
            log.fine("Producer method for General Serializer found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

            if (this.serializerProducerBean != null) {
                log.severe("There can be only one General Serializer producer!");
            }
            this.serializerProducerBean = ppm.getBean();
        }
    }

    <T> void processEventStorageEngineProducerMethod(
            @Observes final ProcessProducerMethod<EventStorageEngine, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for EventStorageEngine found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.eventStorageEngineBean != null) {
            log.severe("There can be only one EventStorageEngine producer!");
        }
        this.eventStorageEngineBean = ppm.getBean();
    }

    <T> void processEntityManagerProviderProducerMethod(
            @Observes final ProcessProducerMethod<EntityManagerProvider, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for EntityManagerProvider found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.entityManagerProviderBean != null) {
            log.severe("There can be only one EntityManagerProvider producer!");
        }
        this.entityManagerProviderBean = ppm.getBean();
    }

    <T> void processTransactionManagerProducerMethod(
            @Observes final ProcessProducerMethod<TransactionManager, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for transactionManagerBean found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.transactionManagerBean != null) {
            log.severe("There can be only one transactionManagerBean producer!");
        }
        this.transactionManagerBean = ppm.getBean();
    }

    <T> void processTokenStoreProducerMethod(
            @Observes final ProcessProducerMethod<TokenStore, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for TokenStore found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.tokenStoreBean != null) {
            log.severe("There can be only one TokenStore producer!");
        }
        this.tokenStoreBean = ppm.getBean();
    }

    <T> void processListenerInvocationErrorHandlerProducerMethod(
            @Observes final ProcessProducerMethod<ListenerInvocationErrorHandler, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for ListenerInvocationErrorHandler found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.listenerInvocationErrorHandlerBean != null) {
            log.severe("There can be only one ListenerInvocationErrorHandler producer!");
        }
        this.listenerInvocationErrorHandlerBean = ppm.getBean();
    }


    <T> void processErrorHandlerProducerMethod(
            @Observes final ProcessProducerMethod<ErrorHandler, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for ErrorHandler found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.errorHandlerBean != null) {
            log.severe("There can be only one ErrorHandler producer!");
        }
        this.errorHandlerBean = ppm.getBean();
    }

    <T> void processQueryUpdateEmitterProducerMethod(
            @Observes final ProcessProducerMethod<QueryUpdateEmitter, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for QueryUpdateEmitter found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.queryUpdateEmitterBean != null) {
            log.severe("There can be only one QueryUpdateEmitter producer!");
        }
        this.queryUpdateEmitterBean = ppm.getBean();
    }

    <T> void processDeadlineManagerBeanProducerMethod(
            @Observes final ProcessProducerMethod<DeadlineManager, T> ppm) {
        // todo handle multiple instances ??
        log.fine("Producer method for DeadlineManager found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.deadlineManagerBean != null) {
            log.severe("There can be only one DeadlineManager producer!");
        }
        this.deadlineManagerBean = ppm.getBean();
    }

    <T> void processEventProcessingModuleProducerMethod(
            @Observes final ProcessProducerMethod<EventProcessingModule, T> ppm) {
        log.fine("Producer method for EventProcessingModule found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        if (this.deadlineManagerBean != null) {
            log.severe("There can be only one EventProcessingModule producer!");
        }
        this.eventProcessingModuleProducerBean = ppm.getBean();
    }

    <T> void processCorrelationDataProviderProducerMethod(
            @Observes final ProcessProducerMethod<CorrelationDataProvider, T> ppm) {
        log.fine("Producer method for ErrorHandler found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        this.correlationDataProviderBeans.add(ppm.getBean());
    }

    <T> void processEventUpcasterProducerMethod(
            @Observes final ProcessProducerMethod<EventUpcaster, T> ppm) {
        log.fine("Producer method for EventUpcaster found: " + ppm.getAnnotatedProducerMethod().getJavaMember().getName());

        this.eventUpcasterBeans.add(ppm.getBean());
    }


    public void afterBeanDiscovery(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager bm) {
        if (!AxonExtension.isExtensionEnabled()) {
            return;
        }

        Configurer configurer;

        if (this.configurerProducer != null) {
            configurer = produce(bm, this.configurerProducer);
            log.info("Using provided Configurer producer: " + configurer.getClass().getSimpleName());
        } else {
            log.info("No configurer producer defined, using default configuration.");
            configurer = DefaultConfigurer.defaultConfiguration();
        }
        if (commandBusProducerBean != null)
            registerComponent(CommandBus.class, configurer::configureCommandBus, configurer,
                    Configuration::commandBus, commandBusProducerBean, bm);

        if (commandGatewayProducerBean != null)
            registerComponent(CommandGateway.class, (c) -> configurer.registerComponent(CommandGateway.class, c),
                    configurer, Configuration::commandGateway, commandGatewayProducerBean, bm);

        if (queryBusProducerBean != null)
            registerComponent(QueryBus.class, configurer::configureQueryBus, configurer,
                    Configuration::queryBus, queryBusProducerBean, bm);

        if (queryGatewayProducerBean != null)
            registerComponent(QueryGateway.class, (c) -> configurer.registerComponent(QueryGateway.class, c),
                    configurer, Configuration::queryGateway, queryGatewayProducerBean, bm);

        if (eventBusProducerBean != null)
            registerComponent(EventBus.class, configurer::configureEventBus, configurer,
                    Configuration::eventBus, eventBusProducerBean, bm);

        if (eventGatewayProducerBean != null)
            registerComponent(EventGateway.class, (c) -> configurer.registerComponent(EventGateway.class, c),
                    configurer, Configuration::eventGateway, eventBusProducerBean, bm);

        if (serializerProducerBean != null) {
            registerComponent(Serializer.class, configurer::configureSerializer,
                    configurer, Configuration::serializer, serializerProducerBean, bm);
        }

        if (eventSerializerProducerBean != null) {
            registerComponent(Serializer.class, configurer::configureEventSerializer,
                    configurer, Configuration::eventSerializer, eventSerializerProducerBean, bm);
        }

        if (messageSerializerProducerBean != null) {
            registerComponent(Serializer.class, configurer::configureMessageSerializer,
                    configurer, Configuration::messageSerializer, messageSerializerProducerBean, bm);
        }

        // event processing module config
        if (eventProcessingModuleProducerBean != null) {
            // todo this doesnt work
            //registerModule(EventProcessingModule.class, configurer::registerModule,
            //        configurer, Configuration::eventProcessingConfiguration, eventProcessingModuleProducerBean, bm);
        }

        // configure other components
        if (eventStorageEngineBean != null) {
            registerComponent(EventStorageEngine.class, configurer::configureEmbeddedEventStore,
                    configurer, Configuration::eventStore, eventStorageEngineBean, bm);
        }
        if (entityManagerProviderBean != null) {
            registerComponent(EntityManagerProvider.class, (f) -> configurer.registerComponent(EntityManagerProvider.class, f),
                    entityManagerProviderBean, bm);
        }
        if (transactionManagerBean != null) {
            registerComponent(TransactionManager.class, configurer::configureTransactionManager,
                    transactionManagerBean, bm);
        }
        if (tokenStoreBean != null) {
            registerComponent(TokenStore.class, (f) -> configurer.registerComponent(TokenStore.class, f),
                    tokenStoreBean, bm);
        }
        if (listenerInvocationErrorHandlerBean != null) {
            registerComponent(ListenerInvocationErrorHandler.class, (f) -> configurer.registerComponent(ListenerInvocationErrorHandler.class, f),
                    listenerInvocationErrorHandlerBean, bm);
        }
        if (errorHandlerBean != null) {
            registerComponent(ErrorHandler.class, (f) -> configurer.registerComponent(ErrorHandler.class, f),
                    errorHandlerBean, bm);
        }
        if (queryUpdateEmitterBean != null) {
            registerComponent(QueryUpdateEmitter.class, configurer::configureQueryUpdateEmitter,
                    configurer, Configuration::queryUpdateEmitter, queryUpdateEmitterBean, bm);
        }
        if (deadlineManagerBean != null) {
            registerComponent(DeadlineManager.class, (f) -> configurer.registerComponent(DeadlineManager.class, f),
                    configurer, Configuration::deadlineManager, deadlineManagerBean, bm);
        }

        configureCorrelationDataProviders(configurer, bm);
        configureEventUpcasters(configurer, bm);

        AxonEventProcessorsConfigurer.registerEventProcessors(configurer);

        registerAggregates(bm, configurer);
        registerMessageHandlers(bm, configurer);

        try {
            Class.forName("org.axonframework.axonserver.connector.AxonServerConfiguration");
            AxonServerConfigurer.registerAxonServer(configurer);
        } catch (Throwable ex) {
            log.warning("No Axon Server configured. If you would like to use Axon Server, " +
                    "please add the `axon-server-connector` dependency.");
        }

        AxonConfiguration configuration = new AxonConfiguration(configurer);

        log.info("Axon Framework configuration complete.");

        log.info("Registering Axon APIs with CDI.");

        afterBeanDiscovery.addBean(new BeanWrapper<Configuration>(Configuration.class, () -> configuration));

        // add axon components beans
        if (this.commandBusProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>(CommandBus.class, configuration::commandBus));
        if (this.commandGatewayProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>(CommandGateway.class, configuration::commandGateway));
        if (this.eventBusProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>(QueryBus.class, configuration::queryBus));
        if (this.eventGatewayProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>(QueryGateway.class, configuration::queryGateway));
        if (this.queryBusProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>(EventBus.class, configuration::eventBus));
        if (this.queryGatewayProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>(EventGateway.class, configuration::eventGateway));

        // add serializers beans
        if (this.serializerProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>(Serializer.class, configuration::serializer));
        if (this.eventSerializerProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>("eventSerializer", Serializer.class, configuration::eventSerializer, Collections.singletonList( new NamedLiteral("eventSerializer"))));
        if (this.messageSerializerProducerBean == null)
            afterBeanDiscovery.addBean(new BeanWrapper<>("messageSerializer", Serializer.class, configuration::messageSerializer, Collections.singletonList(new NamedLiteral("messageSerializer"))));

    }

    @SuppressWarnings("unchecked")
    private void configureCorrelationDataProviders(Configurer configurer, BeanManager bm) {
        configurer.configureCorrelationDataProviders(c ->
                this.correlationDataProviderBeans.stream()
                        .map(b -> getBeanReference(CorrelationDataProvider.class, b, bm))
                        .collect(Collectors.toList()));

        if (!this.correlationDataProviderBeans.isEmpty()) {
            configurer.onInitialize(c -> c.onStart(Integer.MIN_VALUE, () -> {
                this.correlationDataProviderBeans
                        .forEach((Bean bb) -> bm.getContext(bb.getScope()).get(bb, bm.createCreationalContext(bb)));
            }));
        }
    }

    private void configureEventUpcasters(Configurer configurer, BeanManager bm) {
        this.eventUpcasterBeans.forEach(bean ->
                configurer.registerEventUpcaster(c ->
                        getBeanReference(EventUpcaster.class, bean, bm)
                )
        );
    }

    private <T> void registerComponent(Class<T> componentType,
                                       Consumer<Function<Configuration, T>> registrationFunction,
                                       Bean bean,
                                       BeanManager bm) {
        registerComponent(componentType, registrationFunction, null, null, bean, bm);
    }

    @SuppressWarnings("unchecked")
    private <T> void registerComponent(Class<T> componentType,
                                        Consumer<Function<Configuration, T>> registrationFunction,
                                        Configurer configurer,
                                        Consumer<Configuration> initHandler,
                                        Bean bean,
                                        BeanManager bm) {
        registrationFunction.accept(config -> getBeanReference(componentType, bean, bm));
        if (initHandler != null) {
            // todo remove this ? it doesnt work, it works the same
            configurer.onInitialize(c -> c.onStart(Integer.MIN_VALUE, () ->
                bm.getContext(bean.getScope()).get(bean, bm.createCreationalContext(bean))
            ));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getBeanReference(Class<T> componentType, Bean<?> bean, BeanManager bm) {
        return (T) bm.getReference(bean, componentType, bm.createCreationalContext(bean));
    }

    public void afterB(@Observes AfterDeploymentValidation event, BeanManager bm) {
        Configuration c = CDI.current().select(Configuration.class).get();
        c.start();
    }

    @SuppressWarnings("unchecked")
    private <T> void registerAggregateComponent(BeanManager bm, Bean bean, Consumer<Function<Configuration, Repository<T>>> registrationFunction) {
        registrationFunction.accept(config ->
                (Repository) bm.getReference(bean, Object.class, bm.createCreationalContext(bean)));
    }

    /**
     * This was adapted from Axon extension-cdi:
     * https://github.com/AxonFramework/extension-cdi
     */
    @SuppressWarnings("unchecked")
    private void registerAggregates(BeanManager beanManager, Configurer configurer) {
        aggregates.forEach(aggregateDefinition -> {
            log.info("Registering aggregate: " + aggregateDefinition.aggregateType().getSimpleName());

            AggregateConfigurer<?> aggregateConfigurer
                    = AggregateConfigurer.defaultConfiguration(aggregateDefinition.aggregateType());

            if (aggregateDefinition.repository().isPresent()) {
                Bean repoBean = aggregateRepositoryBeansMap.get(aggregateDefinition.repository().get());
                registerAggregateComponent(beanManager, repoBean, aggregateConfigurer::configureRepository);
            } else {
                if (aggregateRepositoryBeansMap.containsKey(aggregateDefinition.repositoryName())) {
                    Bean repoBean = aggregateRepositoryBeansMap.get(aggregateDefinition.repositoryName());
                    registerAggregateComponent(beanManager, repoBean, aggregateConfigurer::configureRepository);
                } else {
                    if (aggregateDefinition.isJpaAggregate()) {
                        aggregateConfigurer.configureRepository(
                                c -> (Repository) GenericJpaRepository.builder(aggregateDefinition.aggregateType())
                                        .entityManagerProvider(c.getComponent(EntityManagerProvider.class))
                                        .eventBus(c.eventBus())
                                        .repositoryProvider(c::repository)
                                        .lockFactory(c.getComponent(LockFactory.class, () -> NullLockFactory.INSTANCE))
                                        .parameterResolverFactory(c.parameterResolverFactory())
                                        .handlerDefinition(c.handlerDefinition(aggregateDefinition.aggregateType()))
                                        .build());
                    }
                    // else use default repository
                }
            }

            configurer.configureAggregate(aggregateConfigurer);
        });
    }

    private void registerMessageHandlers(BeanManager bm, Configurer configurer) {
        this.messageHandlers.forEach(messageHandler -> {
            Component<Object> component = new Component<>(() -> null, "messageHandler",
                    c -> getBeanReference(messageHandler.getBean().getBeanClass(), messageHandler.getBean(), bm)
            );

            if (messageHandler.isCommandHandler()) {
                log.fine("Registering command handler: " +
                        messageHandler.getBean().getBeanClass().getSimpleName());
                configurer.registerCommandHandler(c -> component.get());
            }
            if (messageHandler.isEventHandler()) {
                log.info("Registering event handler: " +
                        messageHandler.getBean().getBeanClass().getSimpleName());
                configurer.eventProcessing(c -> c.registerEventHandler( conf -> component.get()));
            }
            if (messageHandler.isQueryHandler()) {
                log.info("Registering query handler: " +
                        messageHandler.getBean().getBeanClass().getSimpleName());
                configurer.registerQueryHandler(c -> component.get());
            }
        });
    }

    private <T> T produce(BeanManager beanManager, Producer<T> producer) {
        return producer.produce(beanManager.createCreationalContext(null));
    }

}
