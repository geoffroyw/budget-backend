package io.yac.budget.api.resources;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToMany;
import io.katharsis.resource.annotations.JsonApiToOne;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by geoffroy on 07/02/2016.
 */
@JsonApiResource(type = "transactions")
public class TransactionResource {

    @JsonApiId
    private Long id;

    private Integer amountCents;

    private String currency;

    private Integer settlementAmountCents;

    private String settlementCurrency;

    private boolean isSettlementAmountIndicative;

    private Boolean isConfirmed;

    private Date date;

    private String description;

    @JsonApiToMany
    private List<CategoryResource> categories;

    @JsonApiToOne
    private BankAccountResource bankAccount;

    @JsonApiToOne
    private PaymentMeanResource paymentMean;

    public TransactionResource() {
    }

    private TransactionResource(Long id, Integer amountCents, String currency, Integer settlementAmountCents,
                                String settlementCurrency, boolean isSettlementAmountIndicative, Boolean isConfirmed,
                                Date date,
                                String description,
                                List<CategoryResource> categories,
                                BankAccountResource bankAccount, PaymentMeanResource paymentMean) {
        this.id = id;
        this.amountCents = amountCents;
        this.currency = currency;
        this.isConfirmed = isConfirmed;
        this.date = date;
        this.description = description;
        this.categories = categories;
        this.bankAccount = bankAccount;
        this.paymentMean = paymentMean;
        this.settlementAmountCents = settlementAmountCents;
        this.settlementCurrency = settlementCurrency;
        this.isSettlementAmountIndicative = isSettlementAmountIndicative;
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

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSettlementAmountCents() {
        return settlementAmountCents;
    }

    public void setSettlementAmountCents(Integer settlementAmountCents) {
        this.settlementAmountCents = settlementAmountCents;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public boolean isSettlementAmountIndicative() {
        return isSettlementAmountIndicative;
    }

    public void setSettlementAmountIndicative(boolean settlementAmountIndicative) {
        isSettlementAmountIndicative = settlementAmountIndicative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionResource resource = (TransactionResource) o;
        return Objects.equals(id, resource.id) &&
                Objects.equals(amountCents, resource.amountCents) &&
                Objects.equals(currency, resource.currency) &&
                Objects.equals(isConfirmed, resource.isConfirmed) &&
                Objects.equals(description, resource.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountCents, currency, isConfirmed, description);
    }

    @Override
    public String toString() {
        return "TransactionResource{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", amountCents=" + amountCents +
                ", currency='" + currency + '\'' +
                ", isConfirmed=" + isConfirmed +
                ", date=" + date +
                '}';
    }

    public static class Builder {
        private Long id;
        private Integer amountCents;
        private String currency;
        private Boolean isConfirmed;
        private Date date;
        private String description;
        private String type;
        private List<CategoryResource> categories;
        private BankAccountResource bankAccount;
        private PaymentMeanResource paymentMean;
        private Integer settlementAmountCents;
        private String settlementCurrency;
        private boolean isSettlementAmountIndicative;

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

        public Builder isConfirmed(Boolean isConfirmed) {
            this.isConfirmed = isConfirmed;
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

        public Builder settlementAmountCents(Integer amountCents) {
            this.settlementAmountCents = amountCents;
            return this;
        }

        public Builder settlementCurrency(String currency) {
            this.settlementCurrency = currency;
            return this;
        }


        public Builder isSettlementAmountIndicative(boolean isSettlementAmountIndicative) {
            this.isSettlementAmountIndicative = isSettlementAmountIndicative;
            return this;
        }

        public TransactionResource build() {
            return new TransactionResource(id, amountCents, currency, settlementAmountCents, settlementCurrency,
                    isSettlementAmountIndicative, isConfirmed, date, description, categories,
                    bankAccount, paymentMean);
        }
    }
}
