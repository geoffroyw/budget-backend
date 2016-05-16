package io.yac.core.domain;

import io.yac.auth.user.model.User;
import io.yac.core.domain.transaction.Transaction;

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

    private SupportedCurrency currency;

    private String name;

    private List<Transaction> transactions = new ArrayList<>();

    private User owner;

    public BankAccount() {
    }

    private BankAccount(Long id, SupportedCurrency currency, String name, List<Transaction> transactions, User owner) {
        this.id = id;
        this.currency = currency;
        this.name = name;
        this.transactions = transactions;
        this.owner = owner;
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
    @Enumerated(EnumType.STRING)
    public SupportedCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(SupportedCurrency currency) {
        this.currency = currency;
    }

    @OneToMany(targetEntity = Transaction.class, mappedBy = "bankAccount", fetch = FetchType.EAGER)
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
        private SupportedCurrency currency;
        private String name;
        private List<Transaction> transactions;
        private User owner;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder currency(SupportedCurrency currency) {
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

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public BankAccount build() {
            return new BankAccount(id, currency, name, transactions, owner);
        }
    }
}
