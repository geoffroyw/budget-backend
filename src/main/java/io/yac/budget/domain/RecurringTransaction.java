package io.yac.budget.domain;

import io.yac.auth.user.model.User;
import io.yac.budget.schedule.Schedulable;
import io.yac.budget.schedule.temporal.expression.TemporalExpression;
import io.yac.budget.schedule.temporal.expression.TemporalExpression.TemporalExpressionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by geoffroy on 15/02/2016.
 */
@Entity
@Table(name = "recurring_transaction")
public class RecurringTransaction extends TimestampableEntity implements Schedulable {


    private Long id;

    private Integer amountCents;

    private String currency;

    private Date date;

    private String description;

    private PaymentMean paymentMean;

    private BankAccount bankAccount;

    private List<Category> categories = new ArrayList<>();

    private User owner;

    private TemporalExpressionType temporalExpressionType;

    private boolean isActive;

    private Date lastRunOn;

    public RecurringTransaction() {
    }


    public RecurringTransaction(Long id, Integer amountCents, String currency, Date date, String description,
                                PaymentMean paymentMean, BankAccount bankAccount,
                                List<Category> categories, User owner,
                                TemporalExpressionType temporalExpressionType) {
        this.id = id;
        this.amountCents = amountCents;
        this.currency = currency;
        this.date = date;
        this.description = description;
        this.paymentMean = paymentMean;
        this.bankAccount = bankAccount;
        this.categories = categories;
        this.owner = owner;
        this.temporalExpressionType = temporalExpressionType;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recurring_transaction_seq")
    @SequenceGenerator(name = "recurring_transaction_seq", sequenceName = "recurring_transaction_seq")
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
    @Temporal(TemporalType.DATE)
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "recurring_transaction_category",
               joinColumns = {@JoinColumn(name = "recurring_transaction_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Column(name = "temporal_expression_type", nullable = false)
    @Enumerated(EnumType.STRING)
    public TemporalExpressionType getTemporalExpressionType() {
        return temporalExpressionType;
    }

    public void setTemporalExpressionType(
            TemporalExpressionType temporalExpressionType) {
        this.temporalExpressionType = temporalExpressionType;
    }

    @Column(name = "is_active", nullable = false)
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Column(name = "last_run_on", nullable = true)
    @Temporal(TemporalType.DATE)
    public Date getLastRunOn() {
        return lastRunOn;
    }

    public void setLastRunOn(Date lastRunOn) {
        this.lastRunOn = lastRunOn;
    }

    @Override
    @Transient
    public boolean isOccuringOn(Date date) {
        return TemporalExpression.TemporalExpressionFactory.getInstance(temporalExpressionType).includes(date);
    }

    public static class Builder {
        private TemporalExpressionType temporalExpressionType;
        private Long id;
        private Integer amountCents;
        private String currency;
        private Date date;
        private String description;
        private PaymentMean paymentMean;
        private BankAccount bankAccount;
        private List<Category> categories;
        private User owner;

        public Builder temporalExpressionType(TemporalExpressionType temporalExpressionType) {
            this.temporalExpressionType = temporalExpressionType;
            return this;
        }

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

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public RecurringTransaction build() {
            return new RecurringTransaction(id, amountCents, currency, date, description, paymentMean, bankAccount,
                    categories, owner, temporalExpressionType);
        }
    }
}
