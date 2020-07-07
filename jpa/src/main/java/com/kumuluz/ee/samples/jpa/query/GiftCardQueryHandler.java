package com.kumuluz.ee.samples.jpa.query;

import com.kumuluz.ee.samples.jpa.api.FindGiftCardQry;
import com.kumuluz.ee.samples.jpa.api.GiftCardRecord;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

public class GiftCardQueryHandler {

    private final static Logger log = LoggerFactory.getLogger(GiftCardQueryHandler.class);
    private final ConcurrentMap<String, GiftCardRecord> querySideMap;

    public GiftCardQueryHandler(ConcurrentMap querySideMap) {
        this.querySideMap = querySideMap;
    }

    @QueryHandler
    public GiftCardRecord handle(FindGiftCardQry query) {
        log.info("handling {}", query);

        /* Return the record from the repository */
        return querySideMap.getOrDefault(query.getId(), new GiftCardRecord("0", Integer.MIN_VALUE, Integer.MIN_VALUE));
    }
}
