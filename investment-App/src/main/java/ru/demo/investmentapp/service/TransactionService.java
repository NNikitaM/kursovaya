package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.Transaction;
import ru.demo.investmentapp.repository.TransactionDao;

import java.util.List;

public class TransactionService {
    private TransactionDao transactionDao = new TransactionDao();

    public TransactionService() {
    }

    public List<Transaction> findAll() {
        return transactionDao.findAll();
    }

    public Transaction findOne(final long id) {
        return transactionDao.findOne(id);
    }

    public void save(final Transaction entity) {
        if (entity == null)
            return;
        transactionDao.save(entity);
    }

    public void update(final Transaction entity) {
        if (entity == null)
            return;
        transactionDao.update(entity);
    }

    public void delete(final Transaction entity) {
        if (entity == null)
            return;
        transactionDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        transactionDao.deleteById(id);
    }
}
