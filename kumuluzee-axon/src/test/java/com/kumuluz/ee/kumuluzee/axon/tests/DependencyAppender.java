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

import com.kumuluz.ee.testing.arquillian.spi.MavenDependencyAppender;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Adds required dependencies to the deployments.
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
public class DependencyAppender implements MavenDependencyAppender {

    private static final ResourceBundle versionsBundle = ResourceBundle
            .getBundle("META-INF/kumuluzee/axon/versions");

    @Override
    public List<String> addLibraries() {

        List<String> libs = new ArrayList<>();

        libs.add("org.axonframework:axon-server-connector:" + versionsBundle.getString("axon-version"));
        libs.add("org.axonframework:axon-configuration:" + versionsBundle.getString("axon-version"));
        libs.add("org.axonframework:axon-eventsourcing:" + versionsBundle.getString("axon-version"));
        libs.add("org.axonframework:axon-modelling:" + versionsBundle.getString("axon-version"));
        libs.add("org.axonframework:axon-messaging:" + versionsBundle.getString("axon-version"));
        libs.add("com.kumuluz.ee:kumuluzee-jpa-hibernate:3.11.0");
        libs.add("com.kumuluz.ee:kumuluzee-jta-narayana:3.11.0");
        libs.add("com.kumuluz.ee:kumuluzee-servlet-jetty:3.11.0");
        libs.add("org.postgresql:postgresql:42.2.10");

        libs.add("com.beust:jcommander:1.27");

        return libs;
    }
}
