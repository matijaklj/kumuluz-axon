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

package com.kumuluz.ee.kumuluzee.axon;

import com.kumuluz.ee.kumuluzee.axon.properties.AxonServerConfigLoader;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.axonserver.connector.AxonServerConnectionManager;
import org.axonframework.axonserver.connector.util.AxonFrameworkVersionResolver;
import org.axonframework.config.Configurer;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Utility method for registering Axon Server.
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
class AxonServerConfigurer {

    @SuppressWarnings("unchecked")
    static void registerAxonServer(Configurer configurer) {
        AxonServerConfiguration.Builder builder = new AxonServerConfiguration.Builder();

        Map<String, Object> config = AxonServerConfigLoader.getConfig();

        if (config.containsKey("servers"))
            builder.servers((String)config.get("servers"));

        if (config.containsKey("componentName"))
            builder.componentName((String)config.get("componentName"));

        if (config.containsKey("clientId"))
            builder.clientId((String)config.get("clientId"));

        if (config.containsKey("token"))
            builder.token((String)config.get("token"));

        if (config.containsKey("context"))
            builder.context((String)config.get("context"));

        if (config.containsKey("sslEnabled") && (boolean) config.get("sslEnabled") && config.containsKey("certFile"))
            builder.ssl((String)config.get("certFile"));

        if (config.containsKey("initialNrOfPermits") &&
                config.containsKey("nrOfNewPermits") &&
                config.containsKey("newPermitsThreshold"))
            builder.flowControl(Integer.parseInt((String)config.get("initialNrOfPermits")),
                    Integer.parseInt((String)config.get("nrOfNewPermits")),
                    Integer.parseInt((String)config.get("newPermitsThreshold")));

        if (config.containsKey("event") && config.get("event") instanceof Map) {
            Map<String, Object> eventConfig = (Map<String, Object>)config.get("event");

            if (eventConfig.containsKey("initialNrOfPermits") &&
                    eventConfig.containsKey("nrOfNewPermits") &&
                    eventConfig.containsKey("newPermitsThreshold"))
                builder.eventFlowControl(Integer.parseInt((String)eventConfig.get("initialNrOfPermits")),
                    Integer.parseInt((String)eventConfig.get("nrOfNewPermits")),
                    Integer.parseInt((String)eventConfig.get("newPermitsThreshold")));
        }

        if (config.containsKey("command") && config.get("command") instanceof Map) {
            Map<String, Object> commandConfig = (Map<String, Object>)config.get("command");

            if (commandConfig.containsKey("initialNrOfPermits") &&
                    commandConfig.containsKey("nrOfNewPermits") &&
                    commandConfig.containsKey("newPermitsThreshold"))
                builder.commandFlowControl(Integer.parseInt((String)commandConfig.get("initialNrOfPermits")),
                        Integer.parseInt((String)commandConfig.get("nrOfNewPermits")),
                        Integer.parseInt((String)commandConfig.get("newPermitsThreshold")));
        }

        if (config.containsKey("query") && config.get("query") instanceof Map) {
            Map<String, Object> queryConfig = (Map<String, Object>)config.get("query");

            if (queryConfig.containsKey("initialNrOfPermits") &&
                    queryConfig.containsKey("nrOfNewPermits") &&
                    queryConfig.containsKey("newPermitsThreshold"))
                builder.queryFlowControl(Integer.parseInt((String)queryConfig.get("initialNrOfPermits")),
                        Integer.parseInt((String)queryConfig.get("nrOfNewPermits")),
                        Integer.parseInt((String)queryConfig.get("newPermitsThreshold")));
        }

        if (config.containsKey("snapshotPrefetch"))
            builder.snapshotPrefetch(Integer.parseInt((String)config.get("snapshotPrefetch")));

        if (config.containsKey("suppressDownloadMessage") && Boolean.parseBoolean((String)config.get("suppressDownloadMessage")))
            builder.suppressDownloadMessage();

        if (config.containsKey("maxMessageSize"))
            builder.maxMessageSize(Integer.parseInt((String)config.get("maxMessageSize")));

        if (config.containsKey("commandLoadFactor"))
            builder.commandLoadFactor(Integer.parseInt((String)config.get("commandLoadFactor")));

        if (config.containsKey("connectTimeout"))
            builder.connectTimeout(Integer.parseInt((String)config.get("connectTimeout")));

        configurer.registerComponent(AxonServerConfiguration.class, c -> builder.build());

        configurer.registerComponent(AxonServerConnectionManager.class, c -> {
            AxonServerConnectionManager.Builder b = AxonServerConnectionManager.builder()
                            .axonServerConfiguration(c.getComponent(AxonServerConfiguration.class));
            if (c.tags() != null) {
                b.tagsConfiguration(c.tags());
            }
            if (c.getComponent(AxonFrameworkVersionResolver.class) != null) {
                b.axonFrameworkVersionResolver(c.getComponent(AxonFrameworkVersionResolver.class));
            }
            if (c.getComponent(ScheduledExecutorService.class) != null) {
                b.scheduler(c.getComponent(ScheduledExecutorService.class));
            }

            return b.build();
        });
    }
}
