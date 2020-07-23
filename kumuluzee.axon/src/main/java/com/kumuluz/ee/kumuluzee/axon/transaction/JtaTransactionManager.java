package com.kumuluz.ee.kumuluzee.axon.transaction;

import org.axonframework.common.transaction.Transaction;
import org.axonframework.common.transaction.TransactionManager;

/**
 * JtaTransaction
 * This was adapted from Axon extension-cdi:
 * https://github.com/AxonFramework/extension-cdi
 […]
 */
public class JtaTransactionManager implements TransactionManager {

    @Override
    public Transaction startTransaction() {
        return new JtaTransaction();
    }
}
