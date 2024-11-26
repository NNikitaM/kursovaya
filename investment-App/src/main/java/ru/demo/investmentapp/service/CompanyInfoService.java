package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.CompanyInfo;
import ru.demo.investmentapp.repository.CompanyInfoDao;

import java.util.List;

public class CompanyInfoService {
    private CompanyInfoDao companyInfoDao = new CompanyInfoDao();

    public CompanyInfoService() {
    }

    public List<CompanyInfo> findAll() {
        return companyInfoDao.findAll();
    }

    public CompanyInfo findOne(final long id) {
        return companyInfoDao.findOne(id);
    }

    public void save(final CompanyInfo entity) {
        if (entity == null)
            return;
        companyInfoDao.save(entity);
    }

    public void update(final CompanyInfo entity) {
        if (entity == null)
            return;
        companyInfoDao.update(entity);
    }

    public void delete(final CompanyInfo entity) {
        if (entity == null)
            return;
        companyInfoDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        companyInfoDao.deleteById(id);
    }
}
