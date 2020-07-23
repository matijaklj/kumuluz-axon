package com.kumuluz.ee.kumuluzee.axon.properties;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import java.text.MessageFormat;
import java.util.logging.Logger;

public class SerializerProperties {

    private static final Logger log = Logger.getLogger(SerializerProperties.class.getName());

    private final static String AXON_SERIALIZER_CONFIG_KEY = "axon.serializer.";

    // Possible values for these keys are `default`, `xstream`, `java`, and `jackson`.
    private static SerializerType getSerializerType(String serializer) {
        switch (serializer) {
            case "default":
                return SerializerType.DEFAULT;
            case "xstream":
                return SerializerType.XSTREAM;
            case "java":
                return SerializerType.JAVA;
            case "jackson":
                return SerializerType.JACKSON;
            default:
                log.warning(MessageFormat.format("Unknown value `{0}` for Serializer key. Possible values are `default`, `xstream`, `java`, and `jackson`.", serializer));
                return SerializerType.DEFAULT;
        }
    }

    private static SerializerType getSerializerValue(String serializerType) {
        String serializer = "default";
        if (ConfigurationUtil.getInstance().get(AXON_SERIALIZER_CONFIG_KEY + serializerType).isPresent())
            serializer = ConfigurationUtil.getInstance().get(AXON_SERIALIZER_CONFIG_KEY + serializerType).get();

        return getSerializerType(serializer);
    }

    public static SerializerType getGeneralSerializerType() {
        return getSerializerValue("general");
    }

    public static SerializerType getEventSerializerType() {
        return getSerializerValue("event");
    }

    public static SerializerType getMessageSerializerType() {
        return getSerializerValue("message");
    }

    public enum SerializerType {
        XSTREAM,
        JACKSON,
        JAVA,
        DEFAULT;

        SerializerType() {
        }
    }
}
