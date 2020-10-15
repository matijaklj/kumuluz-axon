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

package com.kumuluz.ee.kumuluzee.axon.tests;

import com.kumuluz.ee.kumuluzee.axon.tests.beanz.AggregateRepoBean;
import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestAggregate;
import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestEvent;
import org.axonframework.config.Configuration;
import org.axonframework.modelling.command.Repository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Aggregates tests
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
public class AggregateConfigTest extends Arquillian {

    private static final Logger log = Logger.getLogger(AggregateConfigTest.class.getName());

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(AggregateRepoBean.class)
                .addClass(TestAggregate.class)
                .addClass(TestEvent.class)
                .addAsManifestResource (EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Configuration configuration;

    @Inject
    private Repository<TestAggregate> myRepo;

    @Test
    public void testAggregateIsInjectable() {
        Assert.assertThrows(() -> CDI.current().select(TestAggregate.class).get());
    }

    @Test
    public void testConfiguredAggregate() {
        Repository<TestAggregate> repo = configuration.repository(TestAggregate.class);

        Assert.assertNotNull(repo, "Test aggregate repository shouldn't be null.");

        /*log.info("testing injected aggregate repository + " + repo.getClass().toString());
        try {
            TestEvent event = new TestEvent("msg", 1);
            Message<TestEvent> msg = new GenericEventMessage<>("msg", event, null, Instant.EPOCH);
            UnitOfWork uow = DefaultUnitOfWork.startAndGet(msg);

            uow.execute(() -> {
                try {
                    repo.loadOrCreate("test1", () -> new TestAggregate("test1", 100));
                } catch (Exception ex) {
                    log.warning("wtf " + ex.getMessage());
                }
            });
            uow.commit();
        } catch (Exception e) {
            log.warning(e.getMessage());
            Assert.fail();
        }
        log.info("testing injected aggregate repository + " + myRepo.getClass().toString());
         */

        Assert.assertEquals(myRepo, repo);
    }
}
