package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

public class TestCommand {
    private String id;
    private int testValue;

    public TestCommand(String id, int value) {
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
