package io.yac.budget.api.resources;


import java.util.List;
import java.util.Objects;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class PaymentMeanResource {


    private Long id;

    private String name;

    private String currency;


    private List<Long> transactions;

    public PaymentMeanResource() {
    }

    private PaymentMeanResource(Long id, String name, String currency,
                                List<Long> transactions) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.transactions = transactions;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Long> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Long> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentMeanResource that = (PaymentMeanResource) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, currency);
    }

    @Override
    public String toString() {
        return "PaymentMeanResource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

    public static class Builder {
        private Long id;
        private String name;
        private String currency;
        private List<Long> transactions;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder transactions(List<Long> transactions) {
            this.transactions = transactions;
            return this;
        }

        public PaymentMeanResource build() {
            return new PaymentMeanResource(id, name, currency, transactions);
        }
    }
}
