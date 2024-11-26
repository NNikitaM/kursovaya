package ru.demo.investmentapp.repository;

import ru.demo.investmentapp.model.PortfolioPosition;

public class PortfolioPositionDao extends BaseDao<PortfolioPosition>{
    public PortfolioPositionDao() {
        super(PortfolioPosition.class);
    }
}
