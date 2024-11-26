package ru.demo.investmentapp.repository;

import ru.demo.investmentapp.model.Instrument;

public class InstrumentDao extends BaseDao<Instrument> {
    public InstrumentDao() {
        super(Instrument.class);
    }
}
