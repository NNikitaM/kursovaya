package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "investment_strategies", schema = "public")
public class InvestmentStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "strategy_id")
    private Long strategyId;

    @Column(name = "strategy_name", nullable = false)
    private String strategyName;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    @Column(name = "return_target", nullable = false)
    private Double returnTarget;

    @Column(name = "asset_allocation", nullable = false)
    private String assetAllocation;

    @OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PortfolioStrategy> portfolioStrategies = new HashSet<>();

    public InvestmentStrategy() {
    }

    public InvestmentStrategy(String strategyName, String description, String riskLevel, Double returnTarget, String assetAllocation) {
        this.strategyName = strategyName;
        this.description = description;
        this.riskLevel = riskLevel;
        this.returnTarget = returnTarget;
        this.assetAllocation = assetAllocation;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Double getReturnTarget() {
        return returnTarget;
    }

    public void setReturnTarget(Double returnTarget) {
        this.returnTarget = returnTarget;
    }

    public String getAssetAllocation() {
        return assetAllocation;
    }

    public void setAssetAllocation(String assetAllocation) {
        this.assetAllocation = assetAllocation;
    }

    public Set<PortfolioStrategy> getPortfolioStrategies() {
        return portfolioStrategies;
    }

    public void setPortfolioStrategies(Set<PortfolioStrategy> portfolioStrategies) {
        this.portfolioStrategies = portfolioStrategies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvestmentStrategy investmentStrategy)) return false;
        return Objects.equals(strategyId, investmentStrategy.strategyId) &&
                Objects.equals(strategyName, investmentStrategy.strategyName) &&
                Objects.equals(description, investmentStrategy.description) &&
                Objects.equals(riskLevel, investmentStrategy.riskLevel) &&
                Objects.equals(returnTarget, investmentStrategy.returnTarget) &&
                Objects.equals(assetAllocation, investmentStrategy.assetAllocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strategyId, strategyName, description, riskLevel, returnTarget, assetAllocation);
    }
}
