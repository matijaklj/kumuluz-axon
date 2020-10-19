/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.kumuluz.ee.axon.example.query;

import com.kumuluz.ee.axon.example.api.queries.FindGiftCardQry;
import com.kumuluz.ee.axon.example.api.queries.GiftCardRecord;
import com.kumuluz.ee.axon.example.api.events.IssuedEvt;
import com.kumuluz.ee.axon.example.api.events.RedeemedEvt;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * Axon query side event handlers.
 *
 * @author Matija Kljun
 */
@ApplicationScoped
public class GiftCardEventHandler {

    private final static Logger log = LoggerFactory.getLogger(GiftCardEventHandler.class);

    @Inject
    Configuration configuration;

    @Inject
    private ConcurrentMap<String, GiftCardRecord> querySideMap;

    public GiftCardEventHandler() {

    }

    @EventHandler
    void on(IssuedEvt event) {
        log.info("query side handling {}", event);

        GiftCardRecord record = new GiftCardRecord(event.getId(), event.getAmount(), event.getAmount());
        querySideMap.put(event.getId(), record);

        this.configuration.queryUpdateEmitter().emit(FindGiftCardQry.class, findGiftCardQry -> Objects.equals(event.getId(), findGiftCardQry.getId()), record);
    }

    @EventHandler
    void on(RedeemedEvt event) {
        log.info("query side handling {}", event);

        GiftCardRecord oldRecord = querySideMap.get(event.getId());
        GiftCardRecord record = new GiftCardRecord(event.getId(), oldRecord.getInitialValue(), oldRecord.getRemainingValue() - event.getAmount());
        querySideMap.put(event.getId(), record);

        this.configuration.queryUpdateEmitter().emit(FindGiftCardQry.class, findGiftCardQry -> Objects.equals(event.getId(), findGiftCardQry.getId()), record);
    }
}
