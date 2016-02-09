package io.yac.budget.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
public class PaymentMean extends TimestampableEntity {

    private Long id;

    private String name;

    private String currency;

    private List<Transaction> transactions;

    public PaymentMean() {
    }

    private PaymentMean(Long id, String name, String currency, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.transactions = transactions;
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
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @OneToMany(targetEntity = Transaction.class, mappedBy = "paymentMean")
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String currency;
        private List<Transaction> transactions;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder transactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public PaymentMean build() {
            return new PaymentMean(id, name, currency, transactions);
        }
    }
}
