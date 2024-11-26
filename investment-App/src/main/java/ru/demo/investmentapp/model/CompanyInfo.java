package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "company_info", schema = "public")
public class CompanyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long infoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;


    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "industry", nullable = false)
    private String industry;

    @Column(name = "market_cap", nullable = false)
    private Double marketCap;

    public CompanyInfo() {
    }

    public CompanyInfo(Instrument instrument, String companyName,String industry, Double marketCap) {
        this.instrument = instrument;
        this.companyName = companyName;
        this.industry = industry;
        this.marketCap = marketCap;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyInfo companyInfo)) return false;
        return Objects.equals(infoId, companyInfo.infoId) &&
                Objects.equals(instrument, companyInfo.instrument) &&
                Objects.equals(companyName, companyInfo.companyName) &&
                Objects.equals(industry, companyInfo.industry) &&
                Objects.equals(marketCap, companyInfo.marketCap);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(infoId, instrument, companyName, industry, marketCap);
        final int hashCode = 31 * companyName.hashCode() + 17 * industry.hashCode()
                + 17 * instrument.getInstrumentId().hashCode() + 17 * infoId.hashCode() + 17 * marketCap.hashCode();
        return hashCode;
    }
}
