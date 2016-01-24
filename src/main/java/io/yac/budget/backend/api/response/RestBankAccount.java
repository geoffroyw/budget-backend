package io.yac.budget.backend.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.yac.budget.backend.domain.entity.BankAccount;

import java.util.Currency;

/**
 * Created by geoffroy on 24/01/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("bank-accounts")
public class RestBankAccount {

    private Long id;

    @JsonProperty(value = "currency-code")
    private String currencyCode;


    public RestBankAccount() {
    }

    public RestBankAccount(Long id, String currencyCode) {
        this.id = id;
        this.currencyCode = currencyCode;
    }

    public RestBankAccount(BankAccount bankAccount) {
        this.id = bankAccount.getId();
        this.currencyCode = bankAccount.getCurrency().getCurrencyCode();
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return "RestBankAccount{" +
                "id=" + id +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }

    public BankAccount toBankAccount() {
        return BankAccount.builder().currency(Currency.getInstance(currencyCode)).id(id).build();
    }

    public static class Builder {
        private Long id;
        private String currencyCode;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public RestBankAccount build() {
            return new RestBankAccount(id, currencyCode);
        }
    }
}
