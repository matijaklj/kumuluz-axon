package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class ConfigurerBean {

    @Produces
    @ApplicationScoped
    private Configurer initConfigurer() {
        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        return configurer;
    }
}
