package com.kumuluz.ee.axon.example.api;

public class RedeemedEvt {

    private String id;
    private Integer amount;

    public RedeemedEvt(String id, Integer amount) {
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
