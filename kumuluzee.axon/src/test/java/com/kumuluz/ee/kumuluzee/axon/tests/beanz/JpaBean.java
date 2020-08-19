package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class JpaBean {


    @Produces
    @ApplicationScoped
    public EntityManagerProvider entityManagerProvider() {
        return new TestEntityManagerProvider();
    }

    @Produces
    @ApplicationScoped
    public Configurer configurer(EntityManagerProvider entityManagerProvider) {
        return DefaultConfigurer.jpaConfiguration(entityManagerProvider);
    }
}
