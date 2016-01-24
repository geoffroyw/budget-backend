package io.yac.budget.backend.domain.entity;

import javax.persistence.*;
import java.util.Currency;
import java.util.List;

/**
 * Created by geoffroy on 23/01/2016.
 */
@Entity
@Table(name = "bank_account")
public class BankAccount {

    private Long id;

    private Currency currency;

    private String name;

    private List<Transaction> transactions;

    public BankAccount() {
    }

    private BankAccount(Long id, Currency currency, String name) {
        this.id = id;
        this.currency = currency;
        this.name = name;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @SequenceGenerator(name = "seq_bank_account_id", sequenceName = "seq_bank_account_id", initialValue = 1,
                       allocationSize = 19)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bank_account_id")
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "currency", nullable = false)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(String currencyCode) {
        this.setCurrency(Currency.getInstance(currencyCode));
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @OneToMany(mappedBy = "bankAccount")
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

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", currency=" + currency +
                ", name='" + name + '\'' +
                ", transactions=" + transactions +
                '}';
    }

    public static class Builder {
        private Long id;
        private Currency currency;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public BankAccount build() {
            return new BankAccount(id, currency, name);
        }
    }
}
