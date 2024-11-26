package ru.demo.investmentapp.repository;

import ru.demo.investmentapp.model.MarketData;

public class MarketDataDao extends BaseDao<MarketData>{
    public MarketDataDao () {
        super(MarketData.class);
    }
    public MarketData findByInstrumentId(Long instrumentId) {
        return (MarketData) super.findByInstrumentId(instrumentId);
    }
}
