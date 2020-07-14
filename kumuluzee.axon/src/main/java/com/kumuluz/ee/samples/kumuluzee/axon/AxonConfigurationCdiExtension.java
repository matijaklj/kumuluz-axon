package com.kumuluz.ee.samples.kumuluzee.axon;

import com.kumuluz.ee.configuration.cdi.ConfigValue;
import com.kumuluz.ee.samples.kumuluzee.axon.properties.SerializerProperties;
import com.kumuluz.ee.samples.kumuluzee.axon.stereotype.Aggregate;
import com.kumuluz.ee.samples.kumuluzee.axon.stereotype.AggregateRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
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

    private Producer<Configurer> configurerProducer;
    private Producer<CommandGateway> commandGatewayProducer;
    //private List<Producer<CommandGateway>> commandGatewayProducers = new ArrayList<>();

    @Inject
    private SerializerProperties serializerProperties;

    <T> void processConfigurerProducer(
            @Observes final ProcessProducer<T, Configurer> processProducer) {

        log.debug("Producer for Configurer found: {}.", processProducer.getProducer());

        if (this.configurerProducer != null) {
            log.error("There can be only one Configurer producer!");
        }
        this.configurerProducer = processProducer.getProducer();
    }

    <T> void processCommandGatewayProducer(
            @Observes final ProcessProducer<T, CommandGateway> processProducer) {

        log.debug("Producer for CommandGateway found: {}.", processProducer.getProducer());

        this.commandGatewayProducer = processProducer.getProducer();
        //this.commandGatewayProducers.add(processProducer.getProducer());
    }

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager bm) {

        Configurer configurer;

        if (this.configurerProducer != null) {
            configurer = produce(bm, this.configurerProducer);
        } else {
            log.info("No configurer producer defined, using default configuration.");
            configurer = DefaultConfigurer.defaultConfiguration();
        }

        /*AxonConfiguration axonConfiguration = new AxonConfiguration(configurer);

        afterBeanDiscovery.addBean(new Bean<AxonConfiguration>() {
            @Override
            public Class<AxonConfiguration> getBeanClass() {
                return AxonConfiguration.class;
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return Collections.emptySet();
            }

            @Override
            public boolean isNullable() {
                return false;
            }

            @Override
            public AxonConfiguration create(final CreationalContext<AxonConfiguration> context) {
                return axonConfiguration;
            }

            @Override
            public void destroy(AxonConfiguration instance, final CreationalContext<AxonConfiguration> context) {
                instance.shutdown();
                instance = null;
                context.release();
            }

            @Override
            public Set<Type> getTypes() {
                final Set<Type> types = new HashSet<>();

                types.add(AxonConfiguration.class);
                types.add(Object.class);

                return types;
            }

            @Override
            public Set<Annotation> getQualifiers() {
                final Set<Annotation> qualifiers = new HashSet<>();

                qualifiers.add(new AnnotationLiteral<Default>() {
                });
                qualifiers.add(new AnnotationLiteral<Any>() {
                });

                return qualifiers;
            }

            @Override
            public Class<? extends Annotation> getScope() {
                return ApplicationScoped.class;
            }

            @Override
            public String getName() {
                return AxonConfiguration.class.getSimpleName();
            }

            @Override
            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.emptySet();
            }

            @Override
            public boolean isAlternative() {
                return false;
            }

        });

         */

        CommandGateway commandGateway = null;
        if (this.commandGatewayProducer != null) {
            CommandGateway cmd = produce(bm, this.commandGatewayProducer);
            configurer.registerComponent(CommandGateway.class, c -> cmd);
            commandGateway = cmd;
        }

        Configuration configuration = configurer.start();

        if (commandGateway == null) {
            commandGateway = configuration.commandGateway();

            CommandGateway cmdG = commandGateway;

            afterBeanDiscovery.addBean(new Bean<CommandGateway>() {
                @Override
                public Class<CommandGateway> getBeanClass() {
                    return CommandGateway.class;
                }

                @Override
                public Set<InjectionPoint> getInjectionPoints() {
                    return Collections.emptySet();
                }

                @Override
                public boolean isNullable() {
                    return false;
                }

                @Override
                public CommandGateway create(final CreationalContext<CommandGateway> context) {
                    return cmdG;
                }

                @Override
                public void destroy(CommandGateway instance, final CreationalContext<CommandGateway> context) {
                    instance = null;
                    context.release();
                }

                @Override
                public Set<Type> getTypes() {
                    final Set<Type> types = new HashSet<>();

                    types.add(CommandGateway.class);
                    types.add(Object.class);

                    return types;
                }

                @Override
                public Set<Annotation> getQualifiers() {
                    final Set<Annotation> qualifiers = new HashSet<>();

                    qualifiers.add(new AnnotationLiteral<Default>() {
                    });
                    qualifiers.add(new AnnotationLiteral<Any>() {
                    });

                    return qualifiers;
                }

                @Override
                public Class<? extends Annotation> getScope() {
                    return ApplicationScoped.class;
                }

                @Override
                public String getName() {
                    return CommandGateway.class.getSimpleName();
                }

                @Override
                public Set<Class<? extends Annotation>> getStereotypes() {
                    return Collections.emptySet();
                }

                @Override
                public boolean isAlternative() {
                    return false;
                }
            } );
        }

        afterBeanDiscovery.addBean(new Bean<Configuration>() {
            @Override
            public Class<Configuration> getBeanClass() {
                return Configuration.class;
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return Collections.emptySet();
            }

            @Override
            public boolean isNullable() {
                return false;
            }

            @Override
            public Configuration create(final CreationalContext<Configuration> context) {
                return configuration;
            }

            @Override
            public void destroy(Configuration instance, final CreationalContext<Configuration> context) {
                instance.shutdown();
                instance = null;
                context.release();
            }

            @Override
            public Set<Type> getTypes() {
                final Set<Type> types = new HashSet<>();

                types.add(Configuration.class);
                types.add(Object.class);

                return types;
            }

            @Override
            public Set<Annotation> getQualifiers() {
                final Set<Annotation> qualifiers = new HashSet<>();

                qualifiers.add(new AnnotationLiteral<Default>() {
                });
                qualifiers.add(new AnnotationLiteral<Any>() {
                });

                return qualifiers;
            }

            @Override
            public Class<? extends Annotation> getScope() {
                return ApplicationScoped.class;
            }

            @Override
            public String getName() {
                return Configuration.class.getSimpleName();
            }

            @Override
            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.emptySet();
            }

            @Override
            public boolean isAlternative() {
                return false;
            }

        });

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

    private <T> T produce(BeanManager beanManager, Producer<T> producer, Class<T> clazz) {
        final Set<Bean<?>> beans = beanManager.getBeans(clazz);
        final Bean<T> bean = (Bean<T>) beanManager.resolve(beans);

        final CreationalContext<T> creationalContext = beanManager.createCreationalContext(bean);

        return producer.produce(creationalContext);
    }

    private <T> T produce(BeanManager beanManager, Producer<T> producer) {
        return producer.produce(beanManager.createCreationalContext(null));
    }

}
