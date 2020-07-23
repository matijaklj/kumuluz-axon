package com.kumuluz.ee.kumuluzee.axon.tests;


import com.kumuluz.ee.kumuluzee.axon.tests.beanz.ConfigurerBean;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import java.io.File;

@Test
public class ConfigInjectionTest extends Arquillian {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(ConfigurerBean.class)
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Configuration axonConfig;

    @Test
    public void injectAxonConfigTest() {

        Assert.assertThrows(() -> Assert.assertNull(CDI.current().select(Configurer.class).get()));
        Assert.assertNotNull(axonConfig, "Axon Configuration");
    }

}