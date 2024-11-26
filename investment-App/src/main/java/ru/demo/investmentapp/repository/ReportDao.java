package ru.demo.investmentapp.repository;

import ru.demo.investmentapp.model.Report;

public class ReportDao extends BaseDao<Report>{
    public ReportDao() {
        super(Report.class);
    }
}
