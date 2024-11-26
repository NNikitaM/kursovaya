package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "instruments", schema = "public")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instrument_id")
    private Long instrumentId;

    @Column(name = "instrument_type", nullable = false)
    private String instrumentType;

    @Column(name = "ticker_symbol", nullable = false)
    private String tickerSymbol;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "instrument", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true) // Изменено CascadeType
    private Set<CompanyInfo> companyInfos = new HashSet<>();
    public Instrument() {
    }

    public Instrument(Long instrumentId, String instrumentType, String tickerSymbol, String name) {
        this.instrumentId = instrumentId;
        this.instrumentType = instrumentType;
        this.tickerSymbol = tickerSymbol;
        this.name = name;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CompanyInfo> getCompanyInfos() {
        return companyInfos;
    }

    public void setCompanyInfos(Set<CompanyInfo> companyInfos) {
        this.companyInfos = companyInfos;
    }

//    public CompanyInfo getCompanyInfo() {
//        return companyInfo;
//    }
//
//    public void setCompanyInfo(CompanyInfo companyInfo) {
//        this.companyInfo = companyInfo;
//        if (companyInfo != null) {
//            companyInfo.setInstrument(this);
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrument instrument)) return false;
        return Objects.equals(instrumentId, instrument.instrumentId) &&
                Objects.equals(instrumentType, instrument.instrumentType) &&
                Objects.equals(tickerSymbol, instrument.tickerSymbol) &&
                Objects.equals(name, instrument.name);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(instrumentId, instrumentType, tickerSymbol, name);
        final int hashCode = 31 * name.hashCode() + 17 * instrumentId.hashCode() +
                17 * tickerSymbol.hashCode() + 17 * instrumentType.hashCode();
        return hashCode;
    }
}
