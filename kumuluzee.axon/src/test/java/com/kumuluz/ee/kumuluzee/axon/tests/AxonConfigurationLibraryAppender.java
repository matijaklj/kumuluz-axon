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

import com.kumuluz.ee.kumuluzee.axon.AxonIntegrationCdiExtension;
import com.kumuluz.ee.kumuluzee.axon.AxonExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.CachedAuxilliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Packages KumuluzEE Axon library as a ShrinkWrap archive and adds it to deployments.
 *
 * @author Matija
 * @since 1.0.0
 */
public class AxonConfigurationLibraryAppender extends CachedAuxilliaryArchiveAppender {

    @Override
    protected Archive<?> buildArchive() {

        return ShrinkWrap.create(JavaArchive.class, "kumuluzee-config-axon.jar")
                .addPackages(true, "com.kumuluz.ee.kumuluzee.axon")
                .deletePackages(true, "com.kumuluz.ee.kumuluzee.axon.tests")
                .addAsServiceProvider(com.kumuluz.ee.common.Extension.class, AxonExtension.class)
                .addAsServiceProvider(javax.enterprise.inject.spi.Extension.class,
                        AxonIntegrationCdiExtension.class)
                .addAsResource("META-INF/beans.xml");
    }
}
