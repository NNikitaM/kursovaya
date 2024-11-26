package ru.demo.investmentapp.repository;

import ru.demo.investmentapp.model.Transaction;

public class TransactionDao extends BaseDao<Transaction>{
    public TransactionDao() {
        super(Transaction.class);
    }
}
