package com.kumuluz.ee.kumuluzee.axon.tests.beanz;

import org.axonframework.queryhandling.QueryHandler;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QueryHandlerBean {

    public QueryHandlerBean() {}

    @QueryHandler(queryName = "test-query-multiply-2")
    public Integer handle(Integer num) {
        return num*2;
    }
}
