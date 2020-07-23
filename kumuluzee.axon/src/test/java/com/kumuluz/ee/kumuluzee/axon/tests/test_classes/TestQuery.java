package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;


public class TestQuery {
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
