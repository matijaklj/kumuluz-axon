package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import com.kumuluz.ee.kumuluzee.axon.tests.beanz.ValueChanger;
import org.axonframework.config.Configuration;
import org.axonframework.serialization.upcasting.event.EventUpcaster;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;

@ApplicationScoped
public class TestEventHandler {

    public TestEventHandler() {}

    @Inject
    private ValueChanger valueChanger;
    //Configuration configuration;



    @org.axonframework.eventhandling.EventHandler
    void on(TestEvent event) {
        valueChanger.setValue(event.getTestValue());
        //this.configuration.queryUpdateEmitter().emit(TestQuery.class, testQuery -> Objects.equals(event.getId(), testQuery.getId()), event.getTestValue());
    }
}
