package com.kumuluz.ee.samples.kumuluzee.axon;

import com.kumuluz.ee.samples.kumuluzee.axon.properties.SerializerProperties;
import com.kumuluz.ee.samples.kumuluzee.axon.util.TypesBeanAttributes;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.*;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class AxonConfigurationCdiExtension implements Extension {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    private SerializerProperties serializerProperties;

    List<AnnotatedInstance<Aggregate>> annotatedAggregateInstanceList = new ArrayList<>();
    List<AnnotatedInstance<AggregateRepository>> annotatedAggregateRepoInstanceList = new ArrayList<>();

    private AnnotatedInstance<AxonConfiguration> configurerInstance;

    List<AnnotatedInstance<AxonConfiguration>> commandGatewayInstances = new ArrayList<>();
    private AnnotatedInstance<AxonConfiguration> commandGatewayInstance;
    private AnnotatedInstance<AxonConfiguration> queryGatewayInstance;
    private AnnotatedInstance<AxonConfiguration> eventGatewayInstance;

    private Configuration config;
    private CommandGateway commandGateway;
    private EventGateway eventGateway;
    private QueryGateway queryGateway;

    //AnnotatedType<AxonConfig> annotatedType;

    public <X> void processAxonConfigurationAnnotations(@Observes ProcessBean<X> pat) {
        for (Method method : pat.getBean().getBeanClass().getMethods()) {
            if (method.getAnnotation(AxonConfiguration.class) != null) {

                AxonConfiguration annotation = method.getAnnotation(AxonConfiguration.class);

                if (method.getReturnType().equals(Configurer.class)) {
                    if (this.configurerInstance != null)
                        log.error("only one method can be annotated for axon configuration");

                    this.configurerInstance = new AnnotatedInstance<>(pat.getBean(), method, annotation);
                }

                if (method.getReturnType().equals(CommandGateway.class)) {
                    if (this.commandGatewayInstance != null)
                        log.error("only one method can be annotated for axon CommandGateway");

                    this.commandGatewayInstances.add(new AnnotatedInstance<>(pat.getBean(), method, annotation));
                    //this.commandGatewayInstance = new AnnotatedInstance<>(pat.getBean(), method, annotation);
                }

                if (method.getReturnType().equals(EventGateway.class)) {
                    if (this.eventGatewayInstance != null)
                        log.error("only one method can be annotated for axon EventGateway");

                    this.eventGatewayInstance = new AnnotatedInstance<>(pat.getBean(), method, annotation);
                }


                if (method.getReturnType().equals(QueryGateway.class)) {
                    if (this.queryGatewayInstance != null)
                        log.error("only one method can be annotated for axon QueryGateway");

                    this.queryGatewayInstance = new AnnotatedInstance<>(pat.getBean(), method, annotation);
                }

                /*
                if (isAnnotatedTypeAlreadyPresent) {
                    log.warning("Annotated method @axonConfiguration for type " + pat.getBean().getBeanClass() + " already exists.");

                    // trow error
                }
                instances.add(new AnnotatedInstance<>(pat.getBean(), method, annotation));
                 */
            } else if (method.getAnnotation(AggregateRepository.class) != null) {
                AggregateRepository annotation = method.getAnnotation(AggregateRepository.class);

                // todo check if method return type is of type Repository
                // method.getReturnType()
                // todo check the parameter of the method is configuration
                // method.getParameterTypes()

                // else throw error wrongly annotated aggregateRepository method
                annotatedAggregateRepoInstanceList.add(
                        new AnnotatedInstance<AggregateRepository>(pat.getBean(), method, annotation)
                );
            }
        }

        // find annotated aggregates
        if ( pat.getBean().getBeanClass().getAnnotation(Aggregate.class) != null) {
            Aggregate annotation = pat.getBean().getBeanClass().getAnnotation(Aggregate.class);
            this.annotatedAggregateInstanceList.add(new AnnotatedInstance<>(pat.getBean(), pat.getBean().getBeanClass(), annotation));
        }
    }

    public void createProducerBeans(@Observes AfterBeanDiscovery event, BeanManager bm) {
        // todo here create bean for any annotated gateway method
        // command gateway
        if (this.commandGatewayInstances.isEmpty()) {
            // todo create a default bean from config.getCommandGateway
            String s = "";
        } else
            for (AnnotatedInstance<AxonConfiguration> cmdGatewayInst : this.commandGatewayInstances) {
                final AnnotatedType<?> thisType = bm.createAnnotatedType(cmdGatewayInst.getBean().getBeanClass());
                final AnnotatedMethod<?> producerMethod = thisType.getMethods().stream()
                        .filter(m -> m.getJavaMember().getName().equals(cmdGatewayInst.getMethod().getName()))
                        .findFirst()
                        .get();

                final BeanAttributes<?> producerAttributes = bm.createBeanAttributes(producerMethod);

                log.debug("Registering producer bean for {}", cmdGatewayInst.getMethod().getName());

                final Bean<?> bean =
                        bm.createBean(new DelegatingBeanAttributes<Object>(producerAttributes) {
                                                   @Override
                                                   public final Set<Annotation> getQualifiers() {
                                                       final Set<Annotation> qualifiers = new HashSet<>();
                                                       for (Annotation a : cmdGatewayInst.getMethod().getDeclaredAnnotations()) {
                                                           if (a.annotationType().isAnnotationPresent(Qualifier.class)) {
                                                               qualifiers.add(a);
                                                           }
                                                       }
                                                       //qualifiers.add(Default.Literal.INSTANCE);
                                                       return qualifiers;
                                                   }
                                               },
                                cmdGatewayInst.getBean().getBeanClass(),
                                bm.getProducerFactory(producerMethod, cmdGatewayInst.getBean()));

                event.addBean(bean);
            }
    }

    public void addAxonConfiguration(@Observes AfterDeploymentValidation event, BeanManager bm) {
        Set<Bean<?>> allBeans = bm.getBeans(Object.class, new AnnotationLiteral<Any>() {});

        Object inst = bm.getReference(configurerInstance.getBean(), configurerInstance.getMethod().getDeclaringClass(), bm
                        .createCreationalContext(configurerInstance.getBean()));
        Configurer configurer = AxonConfigurationInitializer.initializeAxonConfigurer();

        AxonConfigurationInitializer.registerAnnotatedAggregates(bm, configurer, annotatedAggregateInstanceList, annotatedAggregateRepoInstanceList);

        configureSerializers(configurer, bm);

        try{
            configurer = (Configurer) configurerInstance.getMethod().invoke(inst, configurer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        this.config = configurer.buildConfiguration();


        // old way of creating gateways

        /*if (this.commandGatewayInstance != null) {
            Object commandGatewayInst = bm.getReference(commandGatewayInstance.getBean(), commandGatewayInstance.getMethod().getDeclaringClass(), bm
                    .createCreationalContext(commandGatewayInstance.getBean()));

            try {
                this.commandGateway = (CommandGateway) commandGatewayInstance.getMethod().invoke(commandGatewayInst, this.config);
            } catch (Exception e) {
                log.warning(e.getMessage());
            }
        }

        if (this.eventGatewayInstance != null) {
            Object eventGatewayInst = bm.getReference(eventGatewayInstance.getBean(), eventGatewayInstance.getMethod().getDeclaringClass(), bm
                    .createCreationalContext(eventGatewayInstance.getBean()));

            try {
                this.eventGateway = (EventGateway) eventGatewayInstance.getMethod().invoke(eventGatewayInst, this.config);
            } catch (Exception e) {
                log.warning(e.getMessage());
            }
        }

        if (this.queryGatewayInstance != null) {
            Object queryGatewayInst = bm.getReference(queryGatewayInstance.getBean(), queryGatewayInstance.getMethod().getDeclaringClass(), bm
                    .createCreationalContext(queryGatewayInstance.getBean()));

            try {
                this.queryGateway = (QueryGateway) queryGatewayInstance.getMethod().invoke(queryGatewayInst, this.config);
            } catch (Exception e) {
                log.warning(e.getMessage());
            }
        }*/

        this.config.start();

        /*for (AnnotatedMethod<? super AxonConfig> m : annotatedType.getMethods()) {
            if (m.getJavaMember().getName().equals(AxonConfig.CONFIGURATION_SET_METHOD_NAME)) {
                Bean<?> b = bm.getBeans("axonConfigurationInitializer").iterator().next();
                Object instance = bm.getReference(b, m.getJavaMember().getDeclaringClass(), bm
                        .createCreationalContext(b));

                try {
                    m.getJavaMember().invoke(instance, this.config);
                } catch (Exception e) {

                }
                break;
            }
        }
        */

    }

    private Configurer configureSerializers(Configurer configurer, BeanManager bm) {
        // todo move to after bean discovery so you can add the serializer beans
        /*Serializer defaultSerializer = serializerProperties.getSerializer();


        configurer.configureSerializer(c -> defaultSerializer);
        configurer.configureEventSerializer(c -> c);
        configurer.configureMessageSerializer(c -> c);

         */

        return configurer;
    }

    Configuration getConfig() {
        return this.config;
    }

    CommandGateway getCommandGateway() {
        return this.commandGateway;
    }

    EventGateway getEventGateway() {
        return this.eventGateway;
    }

    QueryGateway getQueryGateway() {
        return this.queryGateway;
    }
}
