package io.yac.budget.api.resources;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.yac.budget.api.serializer.JsonDateSerializer;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by geoffroy on 19/02/2016.
 */
public class RecurringTransactionResource {


    private Long id;

    private Integer amountCents;

    private String currency;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date lastRunOn;

    private String description;

    private String recurringType;


    private List<Long> categories;


    private Long bankAccount;


    private Long paymentMean;

    private boolean isActive;

    public RecurringTransactionResource() {
    }

    public RecurringTransactionResource(Long id, Integer amountCents, String currency, Date lastRunOn,
                                        String description, String recurringType,
                                        List<Long> categories,
                                        Long bankAccount,
                                        Long paymentMean, boolean isActive) {
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

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public Long getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Long bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getPaymentMean() {
        return paymentMean;
    }

    public void setPaymentMean(Long paymentMean) {
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
        private List<Long> categories;
        private Long bankAccount;
        private Long paymentMean;
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

        public Builder categories(List<Long> categories) {
            this.categories = categories;
            return this;
        }

        public Builder bankAccount(Long bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public Builder paymentMean(Long paymentMean) {
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
