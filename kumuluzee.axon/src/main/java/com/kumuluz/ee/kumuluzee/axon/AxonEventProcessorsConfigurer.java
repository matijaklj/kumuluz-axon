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

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;

/**
 * Registration and configuration for Axon Event processors.
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
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
