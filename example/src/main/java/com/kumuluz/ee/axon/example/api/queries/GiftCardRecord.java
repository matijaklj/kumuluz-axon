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

package com.kumuluz.ee.axon.example.api.queries;

import java.io.Serializable;

/**
 * Gift card query side projection.
 *
 * @author Matija Kljun
 */
public class GiftCardRecord implements Serializable {

    private String id;
    private Integer initialValue;
    private Integer remainingValue;

    public GiftCardRecord(String id, Integer initialValue, Integer remainingValue) {
        this.id = id;
        this.initialValue = initialValue;
        this.remainingValue = remainingValue;
    }

    public GiftCardRecord() {
    }

    public String getId() {
        return id;
    }

    public Integer getInitialValue() {
        return initialValue;
    }

    public Integer getRemainingValue() {
        return remainingValue;
    }

    @Override
    public String toString() {
        return "GiftCardRecord{" +
                "id='" + id + '\'' +
                ", initialValue=" + initialValue +
                ", remainingValue=" + remainingValue +
                '}';
    }
}
