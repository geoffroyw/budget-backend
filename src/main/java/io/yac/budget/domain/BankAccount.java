package io.yac.budget.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
@Table(name = "bank_accounts")
public class BankAccount extends TimestampableEntity {

    private Long id;

    private String currency;

    private String name;

    private List<Transaction> transactions = new ArrayList<>();

    public BankAccount() {
    }

    private BankAccount(Long id, String currency, String name, List<Transaction> transactions) {
        this.id = id;
        this.currency = currency;
        this.name = name;
        this.transactions = transactions;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_seq")
    @SequenceGenerator(name = "bank_account_seq", sequenceName = "bank_account_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "currency", nullable = false)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @OneToMany(targetEntity = Transaction.class, mappedBy = "bankAccount")
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder {
        private Long id;
        private String currency;
        private String name;
        private List<Transaction> transactions;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder transactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public BankAccount build() {
            return new BankAccount(id, currency, name, transactions);
        }
    }
}
