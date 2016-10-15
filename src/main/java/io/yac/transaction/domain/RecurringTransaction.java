package io.yac.transaction.domain;

import io.yac.auth.user.model.User;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.categories.domain.Category;
import io.yac.common.domain.SupportedCurrency;
import io.yac.common.domain.TimestampableEntity;
import io.yac.common.scheduler.Schedulable;
import io.yac.common.scheduler.expression.TemporalExpression;
import io.yac.common.scheduler.expression.TemporalExpression.TemporalExpressionType;
import io.yac.paymentmean.domain.PaymentMean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by geoffroy on 16/05/2016.
 */
@Entity
@Table(name = "recurring_transaction")
public class RecurringTransaction extends TimestampableEntity implements Schedulable {


    private Long id;

    private Integer amountCents;

    private SupportedCurrency currency;

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


    private RecurringTransaction(Long id, Integer amountCents, SupportedCurrency currency, String description,
                                 PaymentMean paymentMean, BankAccount bankAccount,
                                 List<Category> categories, User owner,
                                 TemporalExpressionType temporalExpressionType, Date lastRunOn, boolean isActive) {
        this.id = id;
        this.amountCents = amountCents;
        this.currency = currency;
        this.description = description;
        this.paymentMean = paymentMean;
        this.bankAccount = bankAccount;
        this.categories = categories;
        this.owner = owner;
        this.temporalExpressionType = temporalExpressionType;
        this.lastRunOn = lastRunOn;
        this.isActive = isActive;
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
    @Enumerated(EnumType.STRING)
    public SupportedCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(SupportedCurrency currency) {
        this.currency = currency;
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

    @ElementCollection
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recurring_transaction_category",
               joinColumns = @JoinColumn(name = "recurring_transaction_id"),
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

    @Override public String toString() {
        return "RecurringTransaction{" +
                "id=" + id +
                '}';
    }

    public static class Builder {
        private TemporalExpressionType temporalExpressionType;
        private Long id;
        private Integer amountCents;
        private SupportedCurrency currency;
        private String description;
        private PaymentMean paymentMean;
        private BankAccount bankAccount;
        private List<Category> categories;
        private User owner;
        private Date lastRunOn;
        private boolean isActive;

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

        public Builder currency(SupportedCurrency currency) {
            this.currency = currency;
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


        public Builder lastRunOn(Date lastRunOn) {
            this.lastRunOn = lastRunOn;
            return this;
        }

        public RecurringTransaction build() {
            return new RecurringTransaction(id, amountCents, currency, description, paymentMean, bankAccount,
                    categories, owner, temporalExpressionType, lastRunOn, isActive);
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
    }
}
