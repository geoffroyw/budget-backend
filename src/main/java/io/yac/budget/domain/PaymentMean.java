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


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_mean_seq")
    @SequenceGenerator(name = "payment_mean_seq", sequenceName = "payment_mean_seq", allocationSize = 100)
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

}
