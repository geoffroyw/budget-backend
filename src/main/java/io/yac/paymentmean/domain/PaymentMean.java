package io.yac.paymentmean.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.yac.auth.user.model.User;
import io.yac.bankaccount.domain.TransactionCollectionSerializer;
import io.yac.common.api.View;
import io.yac.common.domain.SupportedCurrency;
import io.yac.common.domain.TimestampableEntity;
import io.yac.transaction.domain.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMean extends TimestampableEntity {

    @JsonView(View.Default.class)
    private Long id;

    @JsonView(View.Default.class)
    private String name;

    @JsonView(View.Default.class)
    private SupportedCurrency currency;

    @JsonView(View.Default.class)
    @JsonSerialize(using = TransactionCollectionSerializer.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentMean that = (PaymentMean) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
