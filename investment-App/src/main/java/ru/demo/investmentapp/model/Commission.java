package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "commissions", schema = "public")
public class Commission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commission_id")
    private Long commissionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(name = "commission_type", nullable = false)
    private String commissionType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    public Commission() {
    }

    public Commission(Transaction transaction, String commissionType, Double amount) {
        this.transaction = transaction;
        this.commissionType = commissionType;
        this.amount = amount;
    }

    public Long getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(Long commissionId) {
        this.commissionId = commissionId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commission commission)) return false;
        return Objects.equals(commissionId, commission.commissionId) &&
                Objects.equals(transaction, commission.transaction) &&
                Objects.equals(commissionType, commission.commissionType) &&
                Objects.equals(amount, commission.amount);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(commissionId, transaction, commissionType, amount);
        final int hashCode = 31 * commissionType.hashCode() + 17 * amount.hashCode() +
                17 * transaction.getTransactionId().hashCode() + 17 * commissionId.hashCode();
        return hashCode;
    }
}
