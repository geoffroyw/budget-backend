package io.yac.rates.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by geoffroy on 18/02/2016.
 */
@Entity
public class ExchangeRate {
    private Long id;
    private BigDecimal amount;
    private Date date;
    private CurrencyRate currencyRate;

    public ExchangeRate() {
    }

    private ExchangeRate(BigDecimal amount, Date date, CurrencyRate currencyRate) {
        this.amount = amount;
        this.date = date;
        this.currencyRate = currencyRate;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_rate_seq")
    @SequenceGenerator(name = "exchange_rate_seq", sequenceName = "exchange_rate_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "amount", nullable = false)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_rate_id")
    public CurrencyRate getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(CurrencyRate currencyRate) {
        this.currencyRate = currencyRate;
    }

    public static class Builder {
        private BigDecimal amount;
        private Date date;
        private CurrencyRate currencyRate;

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder currencyRate(CurrencyRate currencyRate) {
            this.currencyRate = currencyRate;
            return this;
        }

        public ExchangeRate build() {
            return new ExchangeRate(amount, date, currencyRate);
        }
    }
}
