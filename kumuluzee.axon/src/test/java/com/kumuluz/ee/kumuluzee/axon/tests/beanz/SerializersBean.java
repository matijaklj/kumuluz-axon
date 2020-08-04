package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import org.axonframework.serialization.JavaSerializer;
import org.axonframework.serialization.Serializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Test serializers bean
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class SerializersBean {

    @Produces
    @Named("serializer")
    @ApplicationScoped
    public Serializer generalSerializer() {
        return JavaSerializer.builder().build();
    }

    @Produces
    @Named("eventSerializer")
    @ApplicationScoped
    public Serializer eventSerializer() {
        return JavaSerializer.builder().build();
    }

    @Produces
    @Named("messageSerializer")
    @ApplicationScoped
    public Serializer messageSerializer() {
        return JavaSerializer.builder().build();
    }


}
