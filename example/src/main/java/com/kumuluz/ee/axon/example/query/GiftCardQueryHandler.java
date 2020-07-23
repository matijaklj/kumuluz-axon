package com.kumuluz.ee.axon.example.query;

import com.kumuluz.ee.axon.example.api.FindGiftCardQry;
import com.kumuluz.ee.axon.example.api.GiftCardRecord;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class GiftCardQueryHandler {

    private final static Logger log = LoggerFactory.getLogger(GiftCardQueryHandler.class);
    //private final ConcurrentMap<String, GiftCardRecord> querySideMap;

    @Inject
    private ConcurrentMap<String, GiftCardRecord> querySideMap;

    /*public GiftCardQueryHandler(ConcurrentMap querySideMap) {
        this.querySideMap = querySideMap;
    }
     */

    public GiftCardQueryHandler() {}

    @QueryHandler
    public GiftCardRecord handle(FindGiftCardQry query) {
        log.info("handling {}", query);

        /* Return the record from the repository */
        return querySideMap.getOrDefault(query.getId(), new GiftCardRecord("0", Integer.MIN_VALUE, Integer.MIN_VALUE));
    }
}
