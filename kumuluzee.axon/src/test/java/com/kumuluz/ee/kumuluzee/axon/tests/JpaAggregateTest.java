package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.tests.beanz.JpaBean;
import com.kumuluz.ee.kumuluzee.axon.tests.beanz.SerializersBean;
import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class JpaAggregateTest extends Arquillian {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(JpaBean.class)
                .addClass(TestEntityManagerProvider.class)
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource (EmptyAsset.INSTANCE, "META-INF/persistance.xml");
    }

    @Inject
    EntityManagerProvider entityManagerProvider;

    @Test
    public void testQueryHandling() {
        EntityManager em = entityManagerProvider.getEntityManager();

        Assert.assertNotNull(em);
    }


}
