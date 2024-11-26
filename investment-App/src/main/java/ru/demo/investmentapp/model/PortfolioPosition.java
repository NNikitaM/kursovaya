package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "portfolio_positions", schema = "public")
public class PortfolioPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long positionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;

    @Column(name = "purchase_date", nullable = false)
    private java.sql.Date purchaseDate;

    public PortfolioPosition() {
    }

    public PortfolioPosition(Portfolio portfolio, Instrument instrument, Integer quantity, Double purchasePrice, java.sql.Date purchaseDate) {
        this.portfolio = portfolio;
        this.instrument = instrument;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public java.sql.Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(java.sql.Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortfolioPosition portfolioPosition)) return false;
        return Objects.equals(positionId, portfolioPosition.positionId) &&
                Objects.equals(portfolio, portfolioPosition.portfolio) &&
                Objects.equals(instrument, portfolioPosition.instrument) &&
                Objects.equals(quantity, portfolioPosition.quantity) &&
                Objects.equals(purchasePrice, portfolioPosition.purchasePrice) &&
                Objects.equals(purchaseDate, portfolioPosition.purchaseDate);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(positionId, portfolio, instrument, quantity, purchasePrice, purchaseDate);
        final int hashCode = 31 * purchaseDate.hashCode() + 17 * portfolio.getPortfolioId().hashCode() +
                17 * instrument.getInstrumentId().hashCode() + 17 * quantity.hashCode() +
                17 * purchasePrice.hashCode() + 17 * positionId.hashCode();
        return hashCode;
    }
}