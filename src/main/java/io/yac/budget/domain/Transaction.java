package io.yac.budget.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
public class Transaction extends TimestampableEntity {

    private Long id;

    private Integer amountCents;

    private String currency;

    private Date date;

    private String description;

    private Boolean isConfirmed;

    private TransactionType type;

    private PaymentMean paymentMean;

    private BankAccount bankAccount;

    private List<Category> categories;

    public Transaction() {
    }

    private Transaction(Long id, Integer amountCents, String currency, Date date, String description,
                        Boolean isConfirmed,
                        TransactionType type, PaymentMean paymentMean, BankAccount bankAccount,
                        List<Category> categories) {
        this.id = id;
        this.amountCents = amountCents;
        this.currency = currency;
        this.date = date;
        this.description = description;
        this.isConfirmed = isConfirmed;
        this.type = type;
        this.paymentMean = paymentMean;
        this.bankAccount = bankAccount;
        this.categories = categories;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 100)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "amount_cents", nullable = false)
    public Integer getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Integer amountCents) {
        this.amountCents = amountCents;
    }

    @Column(name = "amount_currency", nullable = false)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIME)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "is_confirmed", nullable = false)
    public Boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_mean_id")
    public PaymentMean getPaymentMean() {
        return paymentMean;
    }

    public void setPaymentMean(PaymentMean paymentMean) {
        this.paymentMean = paymentMean;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_account_id")
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @ManyToMany
    @JoinTable(name = "transaction_category",
               joinColumns = {@JoinColumn(name = "transaction_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public static class Builder {
        private Long id;
        private Integer amountCents;
        private String currency;
        private Date date;
        private String description;
        private Boolean isConfirmed;
        private TransactionType type;
        private PaymentMean paymentMean;
        private BankAccount bankAccount;
        private List<Category> categories;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder amountCents(Integer amountCents) {
            this.amountCents = amountCents;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder isConfirmed(Boolean isConfirmed) {
            this.isConfirmed = isConfirmed;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder paymentMean(PaymentMean paymentMean) {
            this.paymentMean = paymentMean;
            return this;
        }

        public Builder bankAccount(BankAccount bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public Builder categories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        public Transaction build() {
            return new Transaction(id, amountCents, currency, date, description, isConfirmed, type, paymentMean,
                    bankAccount, categories);
        }
    }
}
