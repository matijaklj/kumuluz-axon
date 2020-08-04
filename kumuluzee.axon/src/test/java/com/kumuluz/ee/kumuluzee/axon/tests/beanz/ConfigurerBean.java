package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Test configurer bean
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class ConfigurerBean {

    @Produces
    @ApplicationScoped
    private Configurer initConfigurer() {
        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        return configurer;
    }
}
