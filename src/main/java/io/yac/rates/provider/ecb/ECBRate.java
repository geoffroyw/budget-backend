package io.yac.rates.provider.ecb;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by geoffroy on 16/05/2016.
 */
public class ECBRate {

    private String currency;
    private Date date;
    private BigDecimal rate;

    private ECBRate() {
    }

    private ECBRate(String currency, Date date, BigDecimal rate) {
        this.currency = currency;
        this.date = date;
        this.rate = rate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ECBRate{" +
                "currency=" + currency +
                ", date=" + date +
                ", rate=" + rate +
                '}';
    }

    public static class Builder {
        private String currency;
        private Date date;
        private BigDecimal amount;

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public ECBRate build() {
            return new ECBRate(currency, date, amount);
        }
    }
}