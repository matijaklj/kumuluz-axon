package com.kumuluz.ee.samples.kumuluzee.axon;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryGateway;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
@Named("axonConfigurationInitializer")
public class AxonConfig {

    //public static final String CONFIGURATION_METHOD_NAME = "getAxonKumuluzeeConfiguration";
    //public static final String CONFIGURATION_SET_METHOD_NAME = "setAxonKumuluzeeConfiguration";

    //@Inject
    //private AxonConfigurationCdiExtension axonConfigurationCdiExtension;

    private Configuration configuration;
    private CommandGateway commandGateway;
    private QueryGateway queryGateway;
    private EventGateway eventGateway;

    @Produces
    public Configuration getConfiguration() {
        return this.configuration;
    }

    /*@Produces
    public CommandGateway getCommandGateway() {
        if (this.commandGateway == null)
            return this.configuration.commandGateway();
        else return  this.commandGateway;
    }*/

    @Produces
    public EventGateway getEventGateway() {
        return this.eventGateway == null ? this.configuration.eventGateway() : this.eventGateway;
    }

    @Produces
    public QueryGateway getQueryGateway() {
        return this.queryGateway == null ? this.configuration.queryGateway() : this.queryGateway;
    }

    public AxonConfig() {}

    @Inject
    public AxonConfig(AxonConfigurationCdiExtension extension) {
        this.configuration = extension.getConfig();
        this.commandGateway = extension.getCommandGateway();
        this.eventGateway = extension.getEventGateway();
        this.queryGateway = extension.getQueryGateway();
    }

}
