/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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

/**
 * Axon Entity Manager provider for container managed em from KumuluzEE.
 *
 * @author Matija Kljun
 * @since 1.0.0
 */
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

            System.out.println("here here :: "+this.persistenceUnitName);

            PersistenceWrapper wrapper = this.persistenceUnitHolder.getEntityManagerFactory(this.persistenceUnitName);

            emp.emf = wrapper.getEntityManagerFactory();
            emp.transactionType = wrapper.getTransactionType();


            emp.resourceFactory = new PersistenceContextResourceFactory(
                    emp.persistenceUnitName,
                    emp.emf,
                    emp.transactionType,
                    emp.synchronizationType
            );

            return emp;
        }
    }

}