package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import com.kumuluz.ee.kumuluzee.axon.tests.MessageHandlingTest;
import com.kumuluz.ee.kumuluzee.axon.tests.test_classes.TestEvent;
import org.axonframework.eventhandling.EventHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class EventHandlerBean {

    private static final Logger log = Logger.getLogger(EventHandlerBean.class.getName());

    public EventHandlerBean() {}

    @Inject
    private ValueChanger valueChanger;

    @EventHandler
    void on(TestEvent event) {
        valueChanger.setValue(event.getTestValue());
        //this.configuration.queryUpdateEmitter().emit(TestQuery.class, testQuery -> Objects.equals(event.getId(), testQuery.getId()), event.getTestValue());
    }
}
