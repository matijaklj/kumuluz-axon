package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import java.io.Serializable;

/**
 * Test Event class
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
public class TestEvent implements Serializable {

    private String id;
    private int testValue;

    public TestEvent(String id, int value) {
        this.id = id;
        this.testValue = value;
    }

    public String getId() {
        return id;
    }

    public int getTestValue() {
        return testValue;
    }
}
