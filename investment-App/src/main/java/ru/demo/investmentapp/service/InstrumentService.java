package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.Instrument;
import ru.demo.investmentapp.repository.InstrumentDao;

import java.util.List;

public class InstrumentService {
    private InstrumentDao instrumentDao = new InstrumentDao();

    public InstrumentService() {
    }

    public List<Instrument> findAll() {
        return instrumentDao.findAll();
    }

    public Instrument findOne(final long id) {
        return instrumentDao.findOne(id);
    }

    public void save(final Instrument entity) {
        if (entity == null)
            return;
        instrumentDao.save(entity);
    }

    public void update(final Instrument entity) {
        if (entity == null)
            return;
        instrumentDao.update(entity);
    }

    public void delete(final Instrument entity) {
        if (entity == null)
            return;
        instrumentDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        instrumentDao.deleteById(id);
    }

}
