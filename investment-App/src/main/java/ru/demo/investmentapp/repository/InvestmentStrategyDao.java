package ru.demo.investmentapp.repository;

import ru.demo.investmentapp.model.InvestmentStrategy;

public class InvestmentStrategyDao extends BaseDao<InvestmentStrategy>{
    public InvestmentStrategyDao() {
        super(InvestmentStrategy.class);
    }
}
