package com.kumuluz.ee.kumuluzee.axon.tests;

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * Registers {@link AxonConfigurationLibraryAppender} with the Arquillian.
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class AxonArquillianExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        extensionBuilder.service(AuxiliaryArchiveAppender.class, AxonConfigurationLibraryAppender.class);
    }
}
