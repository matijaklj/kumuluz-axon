package com.kumuluz.ee.axon.example.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RedeemCmd {

    @TargetAggregateIdentifier
    private String id;
    private Integer amount;

    public RedeemCmd(String id, Integer amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
