package com.kumuluz.ee.kumuluzee.axon;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.EeComponentDependency;
import com.kumuluz.ee.common.dependencies.EeComponentType;
import com.kumuluz.ee.common.dependencies.EeExtensionDef;
import com.kumuluz.ee.common.dependencies.EeExtensionGroup;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import java.util.logging.Logger;

/**
 * KumuluzEE framework extension for Axon framework
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
@EeExtensionDef(name = "Axon", group = EeExtensionGroup.STREAMING)
@EeComponentDependency(EeComponentType.CDI)
public class AxonExtension implements Extension {

    private static final Logger log = Logger.getLogger(AxonExtension.class.getName());

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {
        log.info("Initialising Kumuluz - Axon extension.");
    }

    @Override
    public void load() {
    }

    @Override
    public boolean isEnabled() {
        return isExtensionEnabled();
    }

    public static boolean isExtensionEnabled() {
        ConfigurationUtil config = ConfigurationUtil.getInstance();

        return config.getBoolean("kumuluzee.axon.enabled")
                .orElse(true);
    }
}
