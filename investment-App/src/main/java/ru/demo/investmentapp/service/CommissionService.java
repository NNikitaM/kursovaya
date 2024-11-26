package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.Commission;
import ru.demo.investmentapp.repository.CommissionDao;

import java.util.List;

public class CommissionService {
    private CommissionDao commissionDao = new CommissionDao();

    public CommissionService() {
    }

    public List<Commission> findAll() {
        return commissionDao.findAll();
    }

    public Commission findOne(final long id) {
        return commissionDao.findOne(id);
    }

    public void save(final Commission entity) {
        if (entity == null)
            return;
        commissionDao.save(entity);
    }

    public void update(final Commission entity) {
        if (entity == null)
            return;
        commissionDao.update(entity);
    }

    public void delete(final Commission entity) {
        if (entity == null)
            return;
        commissionDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        commissionDao.deleteById(id);
    }
}
