package io.yac.budget.backend.domain.entity;

import io.yac.budget.backend.domain.converter.MoneyConverter;
import io.yac.budget.backend.domain.vo.TransactionType;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by geoffroy on 23/01/2016.
 */
@Entity
@Table(name = "transaction")
public class Transaction {
    private Long id;
    private TransactionType type;
    private MonetaryAmount amount;
    private BankAccount bankAccount;
    private Date date;
    private String description;
    private List<Category> categories;


    @Id
    @SequenceGenerator(name = "seq_transaction_id", sequenceName = "seq_transaction_id", initialValue = 1,
                       allocationSize = 19)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transaction_id")
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    @Column(name = "amount", nullable = false)
    @Convert(converter = MoneyConverter.class)
    public MonetaryAmount getAmount() {
        return amount;
    }

    public void setAmount(MonetaryAmount amount) {
        this.amount = amount;
    }


    @ManyToOne(targetEntity = BankAccount.class)
    @JoinColumn(name = "bank_account_id")
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "description", nullable = false, length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(mappedBy = "transactions")
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", bankAccount=" + bankAccount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", categories=" + categories +
                '}';
    }
}
