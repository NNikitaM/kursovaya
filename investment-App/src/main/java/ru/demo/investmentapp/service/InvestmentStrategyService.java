package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.InvestmentStrategy;
import ru.demo.investmentapp.repository.InvestmentStrategyDao;

import java.util.List;

public class InvestmentStrategyService {
    private InvestmentStrategyDao investmentStrategyDao = new InvestmentStrategyDao();

    public InvestmentStrategyService() {
    }

    public List<InvestmentStrategy> findAll() {
        return investmentStrategyDao.findAll();
    }

    public InvestmentStrategy findOne(final long id) {
        return investmentStrategyDao.findOne(id);
    }

    public void save(final InvestmentStrategy entity) {
        if (entity == null)
            return;
        investmentStrategyDao.save(entity);
    }

    public void update(final InvestmentStrategy entity) {
        if (entity == null)
            return;
        investmentStrategyDao.update(entity);
    }

    public void delete(final InvestmentStrategy entity) {
        if (entity == null)
            return;
        investmentStrategyDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        investmentStrategyDao.deleteById(id);
    }
}
