package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

/**
 * Test Command class
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
public class TestCommand implements Serializable {

    @TargetAggregateIdentifier
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
