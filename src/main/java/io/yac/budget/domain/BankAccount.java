package io.yac.budget.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
@Table(name = "bank_accounts")
public class BankAccount extends TimestampableEntity {

    private Long id;

    private String currency;

    private List<Transaction> transactions;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_seq")
    @SequenceGenerator(name = "bank_account_seq", sequenceName = "bank_account_seq", allocationSize = 100)
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


}
