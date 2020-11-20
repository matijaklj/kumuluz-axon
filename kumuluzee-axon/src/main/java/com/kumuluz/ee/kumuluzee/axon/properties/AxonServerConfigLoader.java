package com.kumuluz.ee.kumuluzee.axon.properties;

import com.kumuluz.ee.configuration.enums.ConfigurationValueType;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import java.util.*;
import java.util.logging.Logger;

public class AxonServerConfigLoader {

    private static final Logger log = Logger.getLogger(AxonServerConfigLoader.class.getName());
    private final static String CONFIG_PREFIX = "axon.axonserver";

    public static Map<String, Object> getConfig() {
        List<String> configNames = ConfigurationUtil.getInstance().getMapKeys(CONFIG_PREFIX)
                .orElse(new LinkedList<>());

        return getConfig(configNames,
                CONFIG_PREFIX);
    }

    private static Map<String, Object> getConfig(String prefix) {
        List<String> configNames = ConfigurationUtil.getInstance().getMapKeys(prefix)
                .orElse(new LinkedList<>());

        return getConfig(configNames, prefix);
    }


    private static Map<String, Object> getConfig(List<String> configProps, String configPrefix) {
        ConfigurationUtil confUtil = ConfigurationUtil.getInstance();


        Map<String, Object> prop = new HashMap<>();
        for (String configPropDashed : configProps) {
            try {
                String configPropDotted = configPropDashed.replace('-', '.');
                String configName = configPrefix + "." + configPropDashed;

                if (confUtil.get(configName).isPresent()) {
                    if (confUtil.getType(configName).get().equals(ConfigurationValueType.MAP))
                        prop.put(configPropDotted, getConfig(configName));
                    else
                        prop.put(configPropDotted, confUtil.get(configName).get());
                }
            } catch (Exception e) {
                log.severe("Unable to read configuration " + configPrefix + ": " + e.toString());
            }
        }
        return prop;
    }
}
