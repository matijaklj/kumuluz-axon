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
