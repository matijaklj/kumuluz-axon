package com.kumuluz.ee.kumuluzee.axon.properties;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import java.lang.management.ManagementFactory;

public class AxonServerProperties {

    private final static String AXON_SERVER_CONFIG_KEY = "axon.axonserver.";
    private final static ConfigurationUtil config = ConfigurationUtil.getInstance();

    private final static String servers = "localhost";
    private final static String clientId = ManagementFactory.getRuntimeMXBean().getName();
    private final static String context = "default";
    private final static boolean sslEnabled = false;

    // flowControl
    private final static Integer initialNrOfPermits = 5000;
    private final static Integer nrOfNewPermits = null;
    private final static Integer newPermitsThreshold = null;
    // flowControl event
    private final static Integer eventInitialNrOfPermits = 5000;
    private final static Integer eventNrOfNewPermits = null;
    private final static Integer eventNewPermitsThreshold = null;
    // flowControl query
    private final static Integer queryInitialNrOfPermits = 5000;
    private final static Integer queryNrOfNewPermits = null;
    private final static Integer queryNewPermitsThreshold = null;
    // flowControl command
    private final static Integer commandInitialNrOfPermits = 5000;
    private final static Integer commandNrOfNewPermits = null;
    private final static Integer commandNewPermitsThreshold = null;

    private final static Integer snapshotPrefetch = 1;
    private final static boolean suppressDownloadMessage = false;
    private final static Integer maxMessageSize = 0;
    private final static Integer commandLoadFactor = 100;
    private final static Long connectTimeout = 5000L;

    private static String getConfig(String key, String defaltValue) {
        if (config.get(AXON_SERVER_CONFIG_KEY + key).isPresent())
            return config.get(AXON_SERVER_CONFIG_KEY + key).get();
        else
            return defaltValue;
    }

    private static boolean getConfig(String key, boolean defaltValue) {
        if (config.getBoolean(AXON_SERVER_CONFIG_KEY + key).isPresent())
            return config.getBoolean(AXON_SERVER_CONFIG_KEY + key).get();
        else
            return defaltValue;
    }

    private static Long getConfig(String key, Long defaltValue) {
        if (config.getLong(AXON_SERVER_CONFIG_KEY + key).isPresent())
            return config.getLong(AXON_SERVER_CONFIG_KEY + key).get();
        else
            return defaltValue;
    }

    private static Integer getConfig(String key, Integer defaltValue) {
        if (config.getInteger(AXON_SERVER_CONFIG_KEY + key).isPresent())
            return config.getInteger(AXON_SERVER_CONFIG_KEY + key).get();
        else
            return defaltValue;
    }

    public static String getServers() {
        return getConfig("servers", servers);
    }

    public static  String getClientId() {
        return getConfig("clientId", clientId);
    }

    public static  String getComponentName() {
        return getConfig("componentName", (String) null);
    }

    public static  String getToken() {
        return getConfig("token", (String) null);
    }

    public static  String getContext() {
        return getConfig("context", context);
    }

    public static  String getCertFile() {
        return getConfig("certFile", (String) null);
    }

    public static  boolean isSslEnabled() {
        return getConfig("sslEnabled", sslEnabled);
    }

    public static  Integer getInitialNrOfPermits() {
        return getConfig("initialNrOfPermits", initialNrOfPermits);
    }

    public static  Integer getNrOfNewPermits() {
        return getConfig("nrOfNewPermits", nrOfNewPermits);
    }

    public static  Integer getNewPermitsThreshold() {
        return getConfig("newPermitsThreshold", newPermitsThreshold);
    }

    public static  Integer getEventInitialNrOfPermits() {
        return getConfig("event.initialNrOfPermits", eventInitialNrOfPermits);
    }

    public static  Integer getEventNrOfNewPermits() {
        return getConfig("event.nrOfNewPermits", eventNrOfNewPermits);
    }

    public static  Integer getEventNewPermitsThreshold() {
        return getConfig("event.newPermitsThreshold", eventNewPermitsThreshold);
    }

    public static  Integer getQueryInitialNrOfPermits() {
        return getConfig("query.initialNrOfPermits", queryInitialNrOfPermits);
    }

    public static  Integer getQueryNrOfNewPermits() {
        return getConfig("query.nrOfNewPermits", queryNrOfNewPermits);
    }

    public static  Integer getQueryNewPermitsThreshold() {
        return getConfig("query.newPermitsThreshold", queryNewPermitsThreshold);
    }

    public static  Integer getCommandInitialNrOfPermits() {
        return getConfig("command.initialNrOfPermits", commandInitialNrOfPermits);
    }

    public static Integer getCommandNrOfNewPermits() {
        return getConfig("command.nrOfNewPermits", commandNrOfNewPermits);
    }

    public static  Integer getCommandNewPermitsThreshold() {
        return getConfig("command.newPermitsThreshold", commandNewPermitsThreshold);
    }

    public static  Integer getSnapshotPrefetch() {
        return getConfig("snapshotPrefetch", snapshotPrefetch);
    }

    public static  boolean isSuppressDownloadMessage() {
        return getConfig("suppressDownloadMessage", suppressDownloadMessage);
    }

    public static  Integer getMaxMessageSize() {
        return getConfig("maxMessageSize", maxMessageSize);
    }

    public static  Integer getCommandLoadFactor() {
        return getConfig("commandLoadFactor", commandLoadFactor);
    }

    public static  Long getConnectTimeout() {
        return getConfig("connectTimeout", connectTimeout);
    }

}
