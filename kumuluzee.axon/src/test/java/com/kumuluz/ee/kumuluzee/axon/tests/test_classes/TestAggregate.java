package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import com.kumuluz.ee.kumuluzee.axon.stereotype.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;

import javax.enterprise.context.ApplicationScoped;

@Aggregate
@ApplicationScoped
public class TestAggregate {
    @AggregateIdentifier
    private String id;

    private int value;

}
