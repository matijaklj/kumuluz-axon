package com.kumuluz.ee.kumuluzee.axon;

import com.kumuluz.ee.jpa.common.PersistenceUnitHolder;
import com.kumuluz.ee.jpa.common.PersistenceWrapper;
import com.kumuluz.ee.jpa.common.TransactionType;
import com.kumuluz.ee.jpa.common.injection.PersistenceContextResourceFactory;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.jboss.weld.injection.spi.ResourceReference;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SynchronizationType;

public class ContainerManagedEntityManagerProvider implements EntityManagerProvider {

    private EntityManagerFactory emf;
    private PersistenceContextResourceFactory resourceFactory;
    private String persistenceUnitName;
    private TransactionType transactionType;
    private SynchronizationType synchronizationType;

    private ContainerManagedEntityManagerProvider() {}

    @Override
    public EntityManager getEntityManager() {
        ResourceReference<EntityManager> em = this.resourceFactory.createResource();

        return em.getInstance();
    }

    public static class Builder {
        private String persistenceUnitName;
        private SynchronizationType synchronizationType;
        private PersistenceUnitHolder persistenceUnitHolder;

        public Builder() {
            this.persistenceUnitHolder = PersistenceUnitHolder.getInstance();
            this.persistenceUnitName = persistenceUnitHolder.getDefaultUnitName();
            this.synchronizationType = SynchronizationType.SYNCHRONIZED;
        }

        public Builder persistenceUnitName(String persistenceUnitName){
            this.persistenceUnitName = persistenceUnitName;
            return this;
        }

        public Builder synchronizationType(SynchronizationType synchronizationType){
            this.synchronizationType = synchronizationType;
            return this;
        }

        public ContainerManagedEntityManagerProvider build() {
            ContainerManagedEntityManagerProvider emp = new ContainerManagedEntityManagerProvider();

            emp.persistenceUnitName = this.persistenceUnitName;
            emp.synchronizationType = this.synchronizationType;

            PersistenceWrapper wrapper = this.persistenceUnitHolder.getEntityManagerFactory(this.persistenceUnitName);

            emp.emf = wrapper.getEntityManagerFactory();
            emp.transactionType = wrapper.getTransactionType();


            emp.resourceFactory = new PersistenceContextResourceFactory(
                    emp.persistenceUnitName,
                    emp.emf,
                    emp.transactionType,
                    emp.synchronizationType);

            return emp;
        }
    }

}