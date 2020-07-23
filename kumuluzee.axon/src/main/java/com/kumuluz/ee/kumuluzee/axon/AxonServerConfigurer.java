package com.kumuluz.ee.kumuluzee.axon;

import com.kumuluz.ee.kumuluzee.axon.properties.AxonServerProperties;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.axonserver.connector.AxonServerConnectionManager;
import org.axonframework.axonserver.connector.util.AxonFrameworkVersionResolver;
import org.axonframework.config.Configurer;

import java.util.concurrent.ScheduledExecutorService;

class AxonServerConfigurer {

    static void registerAxonServer(Configurer configurer) {
        AxonServerConfiguration.Builder builder = new AxonServerConfiguration.Builder();

        if (AxonServerProperties.getServers() != null)
            builder.servers(AxonServerProperties.getServers());

        if (AxonServerProperties.getComponentName() != null)
            builder.componentName(AxonServerProperties.getComponentName());

        if (AxonServerProperties.getClientId() != null)
            builder.clientId(AxonServerProperties.getClientId());

        if (AxonServerProperties.getToken() != null)
            builder.token(AxonServerProperties.getToken());

        if (AxonServerProperties.getContext() != null)
            builder.context(AxonServerProperties.getContext());

        if (AxonServerProperties.isSslEnabled() && AxonServerProperties.getCertFile() != null)
            builder.ssl(AxonServerProperties.getCertFile());

        if (AxonServerProperties.getInitialNrOfPermits() != null &&
            AxonServerProperties.getNrOfNewPermits() != null &&
            AxonServerProperties.getNewPermitsThreshold() != null)
            builder.flowControl(AxonServerProperties.getInitialNrOfPermits(),
                    AxonServerProperties.getNrOfNewPermits(),
                    AxonServerProperties.getNewPermitsThreshold());

        // event flow control
        if (AxonServerProperties.getEventInitialNrOfPermits() != null &&
                AxonServerProperties.getEventNrOfNewPermits() != null &&
                AxonServerProperties.getEventNewPermitsThreshold() != null)
            builder.eventFlowControl(AxonServerProperties.getEventInitialNrOfPermits(),
                    AxonServerProperties.getEventNrOfNewPermits(),
                    AxonServerProperties.getEventNewPermitsThreshold());

        // command flow control
        if (AxonServerProperties.getCommandInitialNrOfPermits() != null &&
                AxonServerProperties.getCommandNrOfNewPermits() != null &&
                AxonServerProperties.getCommandNewPermitsThreshold() != null)
            builder.commandFlowControl(AxonServerProperties.getCommandInitialNrOfPermits(),
                    AxonServerProperties.getCommandNrOfNewPermits(),
                    AxonServerProperties.getCommandNewPermitsThreshold());

        // query flow control
        if (AxonServerProperties.getQueryInitialNrOfPermits() != null &&
                AxonServerProperties.getQueryNrOfNewPermits() != null &&
                AxonServerProperties.getQueryNewPermitsThreshold() != null)
            builder.queryFlowControl(AxonServerProperties.getQueryInitialNrOfPermits(),
                    AxonServerProperties.getQueryNrOfNewPermits(),
                    AxonServerProperties.getQueryNewPermitsThreshold());

        if (AxonServerProperties.getSnapshotPrefetch() != null)
            builder.snapshotPrefetch(AxonServerProperties.getSnapshotPrefetch());

        if (AxonServerProperties.isSuppressDownloadMessage())
            builder.suppressDownloadMessage();

        if (AxonServerProperties.getMaxMessageSize() != null)
            builder.maxMessageSize(AxonServerProperties.getMaxMessageSize());

        if (AxonServerProperties.getCommandLoadFactor() != null)
            builder.commandLoadFactor(AxonServerProperties.getCommandLoadFactor());

        if (AxonServerProperties.getConnectTimeout() != null)
            builder.connectTimeout(AxonServerProperties.getConnectTimeout());

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
            /* todo how to configure this values ?
            if (c.getComponent(InstructionAckSource.class) != null) {
                b.instructionAckSource(c.getComponent(InstructionAckSource.class));
            }
            if (c.getComponent(InstructionAckSource.class) != null) {
                b.requestStreamFactory();
            }
             */
            if (c.getComponent(ScheduledExecutorService.class) != null) {
                b.scheduler(c.getComponent(ScheduledExecutorService.class));
            }

            return b.build();
        });
    }
}
