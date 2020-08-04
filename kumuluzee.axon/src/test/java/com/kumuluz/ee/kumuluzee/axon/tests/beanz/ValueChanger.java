package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import javax.enterprise.context.ApplicationScoped;

/**
 * Test bean
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
@ApplicationScoped
public class ValueChanger {
    private int value = 0;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
