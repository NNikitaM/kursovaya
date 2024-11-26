package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "portfolio_strategies", schema = "public")
public class PortfolioStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_strategy_id")
    private Long portfolioStrategyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "strategy_id", nullable = false)
    private InvestmentStrategy strategy;

    public PortfolioStrategy() {
    }

    public PortfolioStrategy(Portfolio portfolio, InvestmentStrategy strategy) {
        this.portfolio = portfolio;
        this.strategy = strategy;
    }

    public Long getPortfolioStrategyId() {
        return portfolioStrategyId;
    }

    public void setPortfolioStrategyId(Long portfolioStrategyId) {
        this.portfolioStrategyId = portfolioStrategyId;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public InvestmentStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(InvestmentStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortfolioStrategy portfolioStrategy)) return false;
        return Objects.equals(portfolioStrategyId, portfolioStrategy.portfolioStrategyId) &&
                Objects.equals(portfolio, portfolioStrategy.portfolio) &&
                Objects.equals(strategy, portfolioStrategy.strategy);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(portfolioStrategyId, portfolio, strategy);
        final int hashCode = 31 * portfolioStrategyId.hashCode() + 17 * portfolio.getPortfolioId().hashCode() +
                17 * strategy.getStrategyId().hashCode();
        return hashCode;
    }
}
