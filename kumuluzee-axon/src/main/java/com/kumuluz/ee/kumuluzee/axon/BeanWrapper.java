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

import org.axonframework.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;

/**
 * BeanWrapper class for creating Bean from Supplier class.
 * This was adapted from Axon extension-cdi:
 * https://github.com/AxonFramework/extension-cdi
 */
public class BeanWrapper<T> implements Bean<T>, PassivationCapable {

    private static final Logger logger = LoggerFactory.getLogger(
            MethodHandles.lookup().lookupClass());

    private final String name;
    private final Class<T> clazz;
    private final Supplier<T> supplier;
    private List<AnnotationLiteral> qualifiers = new ArrayList<>();

    public BeanWrapper(final Class<T> clazz, final Supplier<T> supplier) {
        this(clazz.getSimpleName(), clazz, supplier);
    }

    public BeanWrapper(final String name, final Class<T> clazz, final Supplier<T> supplier) {
        this.name = name;
        this.clazz = clazz;
        this.supplier = supplier;
    }

    public BeanWrapper(final String name, final Class<T> clazz, final Supplier<T> supplier, List<AnnotationLiteral> qualifiers) {
        this.name = name;
        this.clazz = clazz;
        this.supplier = supplier;
        this.qualifiers = qualifiers;
    }

    @Override
    public T create(final CreationalContext<T> context) {
        return this.supplier.get();
    }

    @Override
    public void destroy(T instance, final CreationalContext<T> context) {
        if (clazz.equals(Configuration.class)) {
            logger.info("Shutting down Axon configuration.");
            ((Configuration) instance).shutdown();
        }

        instance = null;
        context.release();
    }

    @Override
    public String getName() {
        return name;
    }

    @SuppressWarnings("serial")
    @Override
    public Set<Annotation> getQualifiers() {
        final Set<Annotation> qualifiers = new HashSet<>();

        if (this.qualifiers.isEmpty()) {
            qualifiers.add(new AnnotationLiteral<Default>() {
            });
            qualifiers.add(new AnnotationLiteral<Any>() {
            });
        } else
            qualifiers.addAll(this.qualifiers);

        return qualifiers;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return ApplicationScoped.class;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<Type> getTypes() {
        final Set<Type> types = new HashSet<>();

        types.add(clazz);
        types.add(Object.class);

        return types;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public String getId() {
        return clazz.toString() + "#" + supplier.toString();
    }
}