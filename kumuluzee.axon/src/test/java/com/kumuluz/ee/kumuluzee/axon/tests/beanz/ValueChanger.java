package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import javax.enterprise.context.ApplicationScoped;

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
