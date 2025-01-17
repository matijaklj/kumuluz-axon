package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import com.kumuluz.ee.kumuluzee.axon.stereotype.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.testng.annotations.Test;

import javax.enterprise.context.ApplicationScoped;

/**
 * Test Aggregate class
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
@Aggregate
@ApplicationScoped
public class TestAggregate {
    @AggregateIdentifier
    private String id;

    private int value;

    public TestAggregate(String id, int value) {
        this.id = id;
        this.value = value;
    }

    public TestAggregate() {}

}
