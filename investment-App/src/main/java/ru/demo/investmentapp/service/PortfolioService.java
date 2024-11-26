package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.Portfolio;
import ru.demo.investmentapp.repository.PortfolioDao;

import java.util.List;

public class PortfolioService {
    private PortfolioDao portfolioDao = new PortfolioDao();

    public PortfolioService() {
    }

    public List<Portfolio> findAll() {
        return portfolioDao.findAll();
    }

    public Portfolio findOne(final long id) {
        return portfolioDao.findOne(id);
    }

    public void save(final Portfolio entity) {
        if (entity == null)
            return;
        portfolioDao.save(entity);
    }

    public void update(final Portfolio entity) {
        if (entity == null)
            return;
        portfolioDao.update(entity);
    }

    public void delete(final Portfolio entity) {
        if (entity == null)
            return;
        portfolioDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        portfolioDao.deleteById(id);
    }
}
