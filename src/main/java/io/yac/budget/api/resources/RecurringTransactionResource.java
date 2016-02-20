package io.yac.budget.api.resources;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToMany;
import io.katharsis.resource.annotations.JsonApiToOne;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by geoffroy on 19/02/2016.
 */
@JsonApiResource(type = "recurring-transactions")
public class RecurringTransactionResource {

    @JsonApiId
    private Long id;

    private Integer amountCents;

    private String currency;

    private Date lastRunOn;

    private String description;

    private String recurringType;

    @JsonApiToMany
    private List<CategoryResource> categories;

    @JsonApiToOne
    private BankAccountResource bankAccount;

    @JsonApiToOne
    private PaymentMeanResource paymentMean;

    private boolean isActive;

    public RecurringTransactionResource() {
    }

    public RecurringTransactionResource(Long id, Integer amountCents, String currency, Date lastRunOn,
                                        String description, String recurringType,
                                        List<CategoryResource> categories,
                                        BankAccountResource bankAccount,
                                        PaymentMeanResource paymentMean, boolean isActive) {
        this.id = id;
        this.amountCents = amountCents;
        this.currency = currency;
        this.lastRunOn = lastRunOn;
        this.description = description;
        this.recurringType = recurringType;
        this.categories = categories;
        this.bankAccount = bankAccount;
        this.paymentMean = paymentMean;
        this.isActive = isActive;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Integer amountCents) {
        this.amountCents = amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getLastRunOn() {
        return lastRunOn;
    }

    public void setLastRunOn(Date lastRunOn) {
        this.lastRunOn = lastRunOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecurringType() {
        return recurringType;
    }

    public void setRecurringType(String recurringType) {
        this.recurringType = recurringType;
    }

    public List<CategoryResource> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryResource> categories) {
        this.categories = categories;
    }

    public BankAccountResource getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountResource bankAccount) {
        this.bankAccount = bankAccount;
    }

    public PaymentMeanResource getPaymentMean() {
        return paymentMean;
    }

    public void setPaymentMean(PaymentMeanResource paymentMean) {
        this.paymentMean = paymentMean;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecurringTransactionResource that = (RecurringTransactionResource) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(amountCents, that.amountCents) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(lastRunOn, that.lastRunOn) &&
                Objects.equals(description, that.description) &&
                Objects.equals(recurringType, that.recurringType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountCents, currency, lastRunOn, description, recurringType);
    }

    @Override
    public String toString() {
        return "RecurringTransactionResource{" +
                "recurringType='" + recurringType + '\'' +
                ", id=" + id +
                ", amountCents=" + amountCents +
                ", currency='" + currency + '\'' +
                ", lastRunOn=" + lastRunOn +
                ", description='" + description + '\'' +
                '}';
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public static class Builder {
        private Long id;
        private Integer amountCents;
        private String currency;
        private Date lastRunOn;
        private String description;
        private String recurringType;
        private List<CategoryResource> categories;
        private BankAccountResource bankAccount;
        private PaymentMeanResource paymentMean;
        private boolean isActive;

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

        public Builder lastRunOn(Date lastRunOn) {
            this.lastRunOn = lastRunOn;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder recurringType(String recurringType) {
            this.recurringType = recurringType;
            return this;
        }

        public Builder categories(List<CategoryResource> categories) {
            this.categories = categories;
            return this;
        }

        public Builder bankAccount(BankAccountResource bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public Builder paymentMean(PaymentMeanResource paymentMean) {
            this.paymentMean = paymentMean;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public RecurringTransactionResource build() {
            return new RecurringTransactionResource(id, amountCents, currency, lastRunOn, description, recurringType,
                    categories, bankAccount, paymentMean, isActive);
        }
    }
}
