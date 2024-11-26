package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.PortfolioPosition;
import ru.demo.investmentapp.repository.PortfolioPositionDao;

import java.util.List;

public class PortfolioPositionService {
    private PortfolioPositionDao portfolioPositionDao = new PortfolioPositionDao();

    public PortfolioPositionService() {
    }

    public List<PortfolioPosition> findAll() {
        return portfolioPositionDao.findAll();
    }

    public PortfolioPosition findOne(final long id) {
        return portfolioPositionDao.findOne(id);
    }

    public void save(final PortfolioPosition entity) {
        if (entity == null)
            return;
        portfolioPositionDao.save(entity);
    }

    public void update(final PortfolioPosition entity) {
        if (entity == null)
            return;
        portfolioPositionDao.update(entity);
    }

    public void delete(final PortfolioPosition entity) {
        if (entity == null)
            return;
        portfolioPositionDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        portfolioPositionDao.deleteById(id);
    }
}
