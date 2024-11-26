package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "transactions", schema = "public")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "transaction_date", nullable = false)
    private Date transactionDate;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commission> commissions = new HashSet<>();

    public Transaction() {
    }

    public Transaction(Account account, Instrument instrument, String transactionType, Double amount, java.sql.Date transactionDate) {
        this.account = account;
        this.instrument = instrument;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Set<Commission> getCommissions() {
        return commissions;
    }

    public void setCommissions(Set<Commission> commissions) {
        this.commissions = commissions;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public java.sql.Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(java.sql.Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction transaction)) return false;
        return Objects.equals(transactionId, transaction.transactionId) &&
                Objects.equals(account, transaction.account) &&
                Objects.equals(instrument, transaction.instrument) &&
                Objects.equals(transactionType, transaction.transactionType) &&
                Objects.equals(amount, transaction.amount) &&
                Objects.equals(transactionDate, transaction.transactionDate);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(transactionId, account, instrument, transactionType, amount, transactionDate);
        final int hashCode = 31 * transactionDate.hashCode() + 17 * amount.hashCode() + 17 * transactionType.hashCode() +
        17 * account.getAccountId().hashCode() + 17 * instrument.getInstrumentId().hashCode() + 17 * transactionId.hashCode();
        return hashCode;
    }
}
