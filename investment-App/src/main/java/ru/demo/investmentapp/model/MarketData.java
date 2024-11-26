package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "market_data", schema = "public")
public class MarketData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id")
    private Long dataId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(name = "data_date", nullable = false)
    private java.sql.Date dataDate;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "volume", nullable = false)
    private Integer volume;

    public MarketData() {
    }

    public MarketData(Instrument instrument, java.sql.Date dataDate, Double price, Integer volume) {
        this.instrument = instrument;
        this.dataDate = dataDate;
        this.price = price;
        this.volume = volume;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public java.sql.Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(java.sql.Date dataDate) {
        this.dataDate = dataDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketData marketData)) return false;
        return Objects.equals(dataId, marketData.dataId) &&
                Objects.equals(instrument, marketData.instrument) &&
                Objects.equals(dataDate, marketData.dataDate) &&
                Objects.equals(price, marketData.price) &&
                Objects.equals(volume, marketData.volume);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(dataId, instrument, dataDate, price, volume);
        final int hashCode = 31 * dataDate.hashCode() + 17 * dataId.hashCode() +
                17 * instrument.getInstrumentId().hashCode() + 17 * price.hashCode() + 17 * volume.hashCode();
        return hashCode;
    }
}
