package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.MarketData;
import ru.demo.investmentapp.repository.MarketDataDao;

import java.util.List;

public class MarketDataService {
    private MarketDataDao marketDataDao = new MarketDataDao();

    public MarketDataService() {
    }

    public List<MarketData> findAll() {
        return marketDataDao.findAll();
    }

    public MarketData findOne(final long id) {
        return marketDataDao.findOne(id);
    }

    public void save(final MarketData entity) {
        if (entity == null)
            return;
        marketDataDao.save(entity);
    }

    public void update(final MarketData entity) {
        if (entity == null)
            return;
        marketDataDao.update(entity);
    }

    public void delete(final MarketData entity) {
        if (entity == null)
            return;
        marketDataDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        marketDataDao.deleteById(id);
    }

    public MarketData findByInstrumentId(Long instrumentId) {
        return marketDataDao.findByInstrumentId(instrumentId);
    }

    public List<MarketData> getAllMarketData() {
        return marketDataDao.findAll();
    }
}
