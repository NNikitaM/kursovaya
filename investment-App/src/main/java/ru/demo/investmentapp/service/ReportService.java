package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.Report;
import ru.demo.investmentapp.repository.ReportDao;

import java.util.List;

public class ReportService {
    private ReportDao reportDao = new ReportDao();

    public ReportService() {
    }

    public List<Report> findAll() {
        return reportDao.findAll();
    }

    public Report findOne(final long id) {
        return reportDao.findOne(id);
    }

    public void save(final Report entity) {
        if (entity == null)
            return;
        reportDao.save(entity);
    }

    public void update(final Report entity) {
        if (entity == null)
            return;
        reportDao.update(entity);
    }

    public void delete(final Report entity) {
        if (entity == null)
            return;
        reportDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        reportDao.deleteById(id);
    }
}
