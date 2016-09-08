package com.sxs.server;

/**
 * Created by g.h on 2016/9/8.
 */

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import javax.transaction.*;

/**
 * @author doggy
 *         Created on 16-5-15.
 */
public class AtomikosJtaPlatfom extends AbstractJtaPlatform {
    private UserTransaction transaction;
    private TransactionManager manager;

    @Override
    protected TransactionManager locateTransactionManager() {
        return manager;
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        return transaction;
    }

    public UserTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(UserTransaction transaction) {
        this.transaction = transaction;
    }

    public TransactionManager getManager() {
        return manager;
    }

    public void setManager(TransactionManager manager) {
        this.manager = manager;
    }
}