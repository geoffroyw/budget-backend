package io.yac.paymentmean.domain;

import io.yac.auth.user.model.User;
import io.yac.core.domain.SupportedCurrency;
import io.yac.core.domain.TimestampableEntity;
import io.yac.core.domain.transaction.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
public class PaymentMean extends TimestampableEntity {

    private Long id;

    private String name;

    private SupportedCurrency currency;

    private List<Transaction> transactions = new ArrayList<>();

    private User owner;

    public PaymentMean() {
    }

    private PaymentMean(Long id, String name, SupportedCurrency currency, List<Transaction> transactions, User owner) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.transactions = transactions;
        this.owner = owner;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_mean_seq")
    @SequenceGenerator(name = "payment_mean_seq", sequenceName = "payment_mean_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    public SupportedCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(SupportedCurrency currency) {
        this.currency = currency;
    }

    @OneToMany(targetEntity = Transaction.class, mappedBy = "paymentMean", fetch = FetchType.EAGER)
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public static class Builder {
        private Long id;
        private String name;
        private SupportedCurrency currency;
        private List<Transaction> transactions;
        private User owner;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder currency(SupportedCurrency currency) {
            this.currency = currency;
            return this;
        }

        public Builder transactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public PaymentMean build() {
            return new PaymentMean(id, name, currency, transactions, owner);
        }
    }
}
