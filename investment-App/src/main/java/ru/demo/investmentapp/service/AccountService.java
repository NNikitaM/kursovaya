package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.Account;
import ru.demo.investmentapp.repository.AccountDao;

import java.util.List;

public class AccountService {
    private AccountDao accountDao = new AccountDao();

    public AccountService() {
    }

    public List<Account> findAll() {
        return accountDao.findAll();
    }

    public Account findOne(final long id) {
        return accountDao.findOne(id);
    }

    public void save(final Account entity) {
        if (entity == null)
            return;
        accountDao.save(entity);
    }

    public void update(final Account entity) {
        if (entity == null)
            return;
        accountDao.update(entity);
    }

    public void delete(final Account entity) {
        if (entity == null)
            return;
        accountDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        accountDao.deleteById(id);
    }
}