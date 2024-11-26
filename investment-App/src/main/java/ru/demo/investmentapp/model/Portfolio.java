package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "portfolios", schema = "public")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long portfolioId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_name", nullable = false)
    private User user;

    @Column(name = "portfolio_type", nullable = true)
    private String portfolioType;

    @Column(name = "portfolio_name", nullable = false)
    private String portfolioName;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PortfolioPosition> portfolioPositions = new HashSet<>();

    public Portfolio() {
    }

    public Portfolio(User user, String portfolioType, String portfolioName) {
        this.user = user;
        this.portfolioType = portfolioType;
        this.portfolioName = portfolioName;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPortfolioType() {
        return portfolioType;
    }

    public void setPortfolioType(String portfolioType) {
        this.portfolioType = portfolioType;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public Set<PortfolioPosition> getPortfolioPositions() {
        return portfolioPositions;
    }

    public void setPortfolioPositions(Set<PortfolioPosition> portfolioPositions) {
        this.portfolioPositions = portfolioPositions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Portfolio portfolio)) return false;
        return Objects.equals(portfolioId, portfolio.portfolioId) &&
                Objects.equals(user, portfolio.user) &&
                Objects.equals(portfolioType, portfolio.portfolioType) &&
                Objects.equals(portfolioName, portfolio.portfolioName);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(portfolioId, user, portfolioType, portfolioName);
        final int hashCode = 31 * portfolioType.hashCode() + 17 * portfolioName.hashCode() +
                 17 + user.getUserName().hashCode() + 17 * portfolioId.hashCode();
        return hashCode;
    }
}
