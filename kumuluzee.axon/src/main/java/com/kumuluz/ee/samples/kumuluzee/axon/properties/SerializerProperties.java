package com.kumuluz.ee.samples.kumuluzee.axon.properties;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;

import javax.enterprise.context.ApplicationScoped;
import java.text.MessageFormat;
import java.util.logging.Logger;

@ApplicationScoped
@ConfigBundle("axon.serializer")
public class SerializerProperties {

    private static final Logger log = Logger.getLogger(SerializerProperties.class.getName());

    private String general;
    private String events;
    private String messages;

    /*
    public Serializer getGeneralSerializer() {
        XStreamSerializer defaultSerializer = XStreamSerializer.builder().build();

        return defaultSerializer;
    }

    public Serializer getEventsSerializer() {
        XStreamSerializer defaultSerializer = XStreamSerializer.builder().build();

        return defaultSerializer;
    }

    public Serializer geMessageSerializer() {
        XStreamSerializer defaultSerializer = XStreamSerializer.builder().build();

        return defaultSerializer;
    }

     */

    // Possible values for these keys are `default`, `xstream`, `java`, and `jackson`.
    public SerializerType getSerializerType(String serializer) {
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

    public SerializerType getGeneral() {
        return getSerializerType(general);
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public SerializerType getEvents() {
        return getSerializerType(events);
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public SerializerType getMessages() {
        return getSerializerType(messages);
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public static enum SerializerType {
        XSTREAM,
        JACKSON,
        JAVA,
        DEFAULT;

        private SerializerType() {
        }
    }
}
