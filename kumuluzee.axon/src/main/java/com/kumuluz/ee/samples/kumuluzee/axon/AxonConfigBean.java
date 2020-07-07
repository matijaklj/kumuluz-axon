package com.kumuluz.ee.samples.kumuluzee.axon;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AxonConfigBean implements Bean<AxonConfig>, PassivationCapable {


        static Set<Type> types;
        private Configuration configuration;
        private CommandGateway commandGateway;
        private final Set<Annotation> qualifiers = new HashSet<>();

        public AxonConfigBean(Configuration configuration, CommandGateway commandGateway) {
            this.configuration = configuration;
            this.commandGateway = commandGateway;
            qualifiers.add(new AnnotationLiteral<Default>() {});
            qualifiers.add(new AnnotationLiteral<Any>() {
            });
        }

        public void setConfiguration(Configuration configuration) {
            this.configuration = configuration;
        }

        @Override
        public Class<?> getBeanClass() {
            return AxonConfig.class;
        }

        @Override
        public Set<InjectionPoint> getInjectionPoints() {
            return Collections.EMPTY_SET;
        }

        @Override
        public boolean isNullable() {
            return false;
        }

        @Override
        public Set<Type> getTypes() {
            return types;
        }

        @Override
        public Set<Annotation> getQualifiers() {
            return qualifiers;
        }

        @Override
        public Class<? extends Annotation> getScope() {
            return ApplicationScoped.class;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Set<Class<? extends Annotation>> getStereotypes() {
            return Collections.EMPTY_SET;
        }

        @Override
        public boolean isAlternative() {
            return false;
        }

        @Override
        public AxonConfig create(CreationalContext<AxonConfig> creationalContext) {
            return new AxonConfig();
        }

        @Override
        public void destroy(AxonConfig instance, CreationalContext<AxonConfig> creationalContext) {
        }

        @Override
        public String getId() {
            return getClass().toString();
        }
    }