package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.PortfolioStrategy;
import ru.demo.investmentapp.repository.PortfolioStrategyDao;

import java.util.List;

public class PortfolioStrategyService {
    private PortfolioStrategyDao portfolioStrategyDao = new PortfolioStrategyDao();

    public PortfolioStrategyService() {
    }

    public List<PortfolioStrategy> findAll() {
        return portfolioStrategyDao.findAll();
    }

    public PortfolioStrategy findOne(final long id) {
        return portfolioStrategyDao.findOne(id);
    }

    public void save(final PortfolioStrategy entity) {
        if (entity == null)
            return;
        portfolioStrategyDao.save(entity);
    }

    public void update(final PortfolioStrategy entity) {
        if (entity == null)
            return;
        portfolioStrategyDao.update(entity);
    }

    public void delete(final PortfolioStrategy entity) {
        if (entity == null)
            return;
        portfolioStrategyDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        portfolioStrategyDao.deleteById(id);
    }
}
