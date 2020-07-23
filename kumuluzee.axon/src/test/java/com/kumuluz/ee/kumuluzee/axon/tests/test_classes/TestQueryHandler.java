package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import org.axonframework.queryhandling.QueryHandler;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TestQueryHandler {

    public TestQueryHandler() {}

    @QueryHandler(queryName = "test-query-multiply-2")
    public Integer handle(Integer num) {
        return num*2;
    }
}
