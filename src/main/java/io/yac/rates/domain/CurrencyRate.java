package io.yac.rates.domain;


import io.yac.rates.exceptions.NoRateException;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by geoffroy on 18/02/2016.
 */
@Entity
public class CurrencyRate {


    private Long id;
    private String currency;
    private Date lastUpdatedOn;
    private List<ExchangeRate> rates;

    public CurrencyRate() {
    }

    private CurrencyRate(Long id, String currency, Date lastUpdatedOn,
                         List<ExchangeRate> rates) {
        this.id = id;
        this.currency = currency;
        this.lastUpdatedOn = lastUpdatedOn;
        this.rates = rates;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_rate_seq")
    @SequenceGenerator(name = "currency_rate_seq", sequenceName = "currency_rate_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "last_update_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    @OneToMany(targetEntity = ExchangeRate.class, mappedBy = "currencyRate", fetch = FetchType.LAZY)
    @OrderBy("date DESC")
    public List<ExchangeRate> getRates() {
        return rates;
    }

    public void setRates(List<ExchangeRate> rates) {
        this.rates = rates;
    }

    @Transient
    public ExchangeRate getLatestRate() throws NoRateException {
        if (getRates() == null || getRates().isEmpty()) {
            throw new NoRateException("No rate for currency " + getCurrency());
        }
        return getRates().get(0);
    }

    public static class Builder {
        private Long id;
        private String currency;
        private Date lastUpdatedOn;
        private List<ExchangeRate> rates;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder lastUpdatedOn(Date lastUpdatedOn) {
            this.lastUpdatedOn = lastUpdatedOn;
            return this;
        }

        public Builder rates(List<ExchangeRate> rates) {
            this.rates = rates;
            return this;
        }

        public CurrencyRate build() {
            return new CurrencyRate(id, currency, lastUpdatedOn, rates);
        }
    }
}
