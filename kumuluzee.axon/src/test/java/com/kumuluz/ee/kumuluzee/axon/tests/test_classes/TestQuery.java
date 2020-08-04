package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import java.io.Serializable;

/**
 * Test Query class
 *
 * @author Matija Kljun
 * @since 0.0.1
 */
public class TestQuery implements Serializable {

    private String id;
    private int testValue;

    public TestQuery(String id, int value) {
        this.id = id;
        testValue = value;
    }

    public String getId() {
        return id;
    }

    public int getTestValue() {
        return testValue;
    }
}
