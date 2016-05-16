package io.yac.core.domain.transaction;

import io.yac.auth.user.model.User;
import io.yac.core.domain.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
public class Transaction extends TimestampableEntity {

    private Long id;

    private Integer amountCents;

    private SupportedCurrency currency;

    private Integer settlementAmountCents;

    private SupportedCurrency settlementCurrency;

    private Date date;

    private String description;

    private Boolean isConfirmed;

    private PaymentMean paymentMean;

    private BankAccount bankAccount;

    private List<Category> categories = new ArrayList<>();

    private User owner;

    public Transaction() {
    }

    private Transaction(Long id, Integer amountCents, SupportedCurrency currency, Integer settlementAmountCents,
                        SupportedCurrency settlementCurrency, Date date, String description,
                        Boolean isConfirmed, PaymentMean paymentMean, BankAccount bankAccount,
                        List<Category> categories, User owner) {
        this.id = id;
        this.amountCents = amountCents;
        this.currency = currency;
        this.date = date;
        this.description = description;
        this.isConfirmed = isConfirmed;
        this.paymentMean = paymentMean;
        this.bankAccount = bankAccount;
        this.categories = categories;
        this.owner = owner;
        this.settlementCurrency = settlementCurrency;
        this.settlementAmountCents = settlementAmountCents;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq")
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

    @Column(name = "is_confirmed", nullable = false)
    public Boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
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
    @JoinTable(name = "transaction_category",
               joinColumns = {@JoinColumn(name = "transaction_id", referencedColumnName = "id")},
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

    @Column(name = "settlement_amount_cents", nullable = true)
    public Integer getSettlementAmountCents() {
        return settlementAmountCents;
    }

    public void setSettlementAmountCents(Integer settlementAmountCents) {
        this.settlementAmountCents = settlementAmountCents;
    }

    @Column(name = "settlement_amount_currency", nullable = true)
    @Enumerated(EnumType.STRING)
    public SupportedCurrency getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SupportedCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public static class Builder {
        private Long id;
        private Integer amountCents;
        private SupportedCurrency currency;
        private Date date;
        private String description;
        private Boolean isConfirmed;
        private PaymentMean paymentMean;
        private BankAccount bankAccount;
        private List<Category> categories;
        private User owner;
        private Integer settlementAmountCents;
        private SupportedCurrency settlementCurrency;

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

        public Builder settlementAmountCents(Integer amountCents) {
            this.settlementAmountCents = amountCents;
            return this;
        }

        public Builder settlementCurrency(SupportedCurrency currency) {
            this.settlementCurrency = currency;
            return this;
        }

        public Transaction build() {
            return new Transaction(id, amountCents, currency, settlementAmountCents, settlementCurrency, date, description, isConfirmed, paymentMean,
                    bankAccount, categories, owner);
        }
    }
}
