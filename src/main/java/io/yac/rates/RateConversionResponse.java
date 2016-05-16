package io.yac.rates;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by geoffroy on 18/02/2016.
 */
public class RateConversionResponse {

    private Date asOf;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amountInFromCurrency;
    private BigDecimal amountInToCurrency;


    public RateConversionResponse(Date asOf, String fromCurrency, String toCurrency,
                                  BigDecimal amountInFromCurrency, BigDecimal amountInToCurrency) {
        this.asOf = asOf;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amountInFromCurrency = amountInFromCurrency;
        this.amountInToCurrency = amountInToCurrency;
    }

    public Date getAsOf() {
        return asOf;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public BigDecimal getAmountInFromCurrency() {
        return amountInFromCurrency;
    }

    public BigDecimal getAmountInToCurrency() {
        return amountInToCurrency;
    }

    @Override
    public String toString() {
        return "RateConversionResponse{" +
                "asOf=" + asOf +
                ", fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", amountInFromCurrency=" + amountInFromCurrency +
                ", amountInToCurrency=" + amountInToCurrency +
                '}';
    }
}
