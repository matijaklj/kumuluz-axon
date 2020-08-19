package com.kumuluz.ee.kumuluzee.axon;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;

class AxonEventProcessorsConfigurer {

    private final static String AXON_EVENT_PROC_CONFIG_KEY = "axon.eventhandling.processors";
    private final static ConfigurationUtil config = ConfigurationUtil.getInstance();

    static void registerEventProcessors(Configurer configurer) {
        if (config.getMapKeys(AXON_EVENT_PROC_CONFIG_KEY).isPresent()){
            for(String key : config.getMapKeys(AXON_EVENT_PROC_CONFIG_KEY).get()) {
                if (config.get(AXON_EVENT_PROC_CONFIG_KEY + "." + key + ".mode").isPresent()) {
                    String mode = config.get(AXON_EVENT_PROC_CONFIG_KEY + "." + key + ".mode").get();

                    if (mode.equals("subscribing")){
                        configurer.eventProcessing()
                                .registerSubscribingEventProcessor(key);
                    } else if (mode.equals("tracking")) {
                        TrackingEventProcessorConfiguration tepConfig = getTrackingEventProcessorConfig(key);

                        configurer.eventProcessing()
                                .registerTrackingEventProcessor(
                                        key,
                                        Configuration::eventStore,
                                        c -> tepConfig
                                );
                    }
                }
            }
        }
    }

    static TrackingEventProcessorConfiguration getTrackingEventProcessorConfig(String key) {
        int threadCount = 1;
        if (config.getInteger(AXON_EVENT_PROC_CONFIG_KEY + "." + key + ".threadCount").isPresent())
            threadCount = config.getInteger(AXON_EVENT_PROC_CONFIG_KEY + "." + key + ".threadCount").get();

        TrackingEventProcessorConfiguration trackingConfig = TrackingEventProcessorConfiguration
                .forParallelProcessing(threadCount);

        if (config.getInteger(AXON_EVENT_PROC_CONFIG_KEY + "." + key + ".initialSegmentCount").isPresent()) {
            int initialSegmentCount = config.getInteger(AXON_EVENT_PROC_CONFIG_KEY + "." + key + ".initialSegmentCount").get();
            trackingConfig.andInitialSegmentsCount(initialSegmentCount);
        }

        return trackingConfig;
    }
}
