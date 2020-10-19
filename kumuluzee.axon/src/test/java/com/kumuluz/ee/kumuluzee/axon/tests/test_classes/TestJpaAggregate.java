package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import com.kumuluz.ee.kumuluzee.axon.stereotype.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@ApplicationScoped
@Aggregate
@Entity
public class TestJpaAggregate implements Serializable {
    @Id
    @AggregateIdentifier
    private String id;
    private String name;

    public TestJpaAggregate() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
