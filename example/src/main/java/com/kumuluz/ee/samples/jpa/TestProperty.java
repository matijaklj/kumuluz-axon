package com.kumuluz.ee.samples.jpa;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TestProperty {
    private int value = 99;

    public int getValue() {
        return this.value;
    }
}
