package com.kumuluz.ee.axon.example.query;

import com.kumuluz.ee.axon.example.api.FindGiftCardQry;
import com.kumuluz.ee.axon.example.api.GiftCardRecord;
import com.kumuluz.ee.axon.example.api.IssuedEvt;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class GiftCardEventHandler {

    private final static Logger log = LoggerFactory.getLogger(GiftCardEventHandler.class);

    @Inject
    Configuration configuration;
    //private QueryUpdateEmitter queryUpdateEmitter;
    @Inject
    private ConcurrentMap<String, GiftCardRecord> querySideMap;

    /*@Inject
    public GiftCardEventHandler(Configuration configuration, ConcurrentMap querySideMap) {
        this.queryUpdateEmitter = configuration.queryUpdateEmitter();
        this.querySideMap = querySideMap;
    }

     */
    public GiftCardEventHandler() {

    }

    @EventHandler
    void on(IssuedEvt event) {
        log.info("handling {}", event);

        /* Save the record */
        GiftCardRecord record = new GiftCardRecord(event.getId(), event.getAmount(), event.getAmount());
        querySideMap.put(event.getId(), record);

        /* Send it to subscription queries of type FindGiftCardQry, but only if the gift card id matches. */
        this.configuration.queryUpdateEmitter().emit(FindGiftCardQry.class, findGiftCardQry -> Objects.equals(event.getId(), findGiftCardQry.getId()), record);
    }
}
