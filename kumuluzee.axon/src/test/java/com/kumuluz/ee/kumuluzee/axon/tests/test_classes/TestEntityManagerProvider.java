package com.kumuluz.ee.kumuluzee.axon.tests.test_classes;

import org.axonframework.common.jpa.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class TestEntityManagerProvider implements EntityManagerProvider {

    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}