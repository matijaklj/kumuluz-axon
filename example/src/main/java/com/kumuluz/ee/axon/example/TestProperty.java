package com.kumuluz.ee.axon.example;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TestProperty {
    private int value = 99;

    public int getValue() {
        return this.value;
    }
}
