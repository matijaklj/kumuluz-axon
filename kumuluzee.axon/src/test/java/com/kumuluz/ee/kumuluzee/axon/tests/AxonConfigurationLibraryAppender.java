package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.AxonConfigurationCdiExtension;
import com.kumuluz.ee.kumuluzee.axon.AxonExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.CachedAuxilliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class AxonConfigurationLibraryAppender extends CachedAuxilliaryArchiveAppender {

    @Override
    protected Archive<?> buildArchive() {

        return ShrinkWrap.create(JavaArchive.class, "kumuluzee-config-axon.jar")
                .addPackages(true, "com.kumuluz.ee.kumuluzee.axon")
                .deletePackages(true, "com.kumuluz.ee.kumuluzee.axon.tests")
                .addAsServiceProvider(com.kumuluz.ee.common.Extension.class, AxonExtension.class)
                .addAsServiceProvider(javax.enterprise.inject.spi.Extension.class,
                        AxonConfigurationCdiExtension.class)
                .addAsResource("META-INF/beans.xml");
    }
}
