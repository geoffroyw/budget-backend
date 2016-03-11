package io.yac.services.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by geoffroy on 10/03/2016.
 */
@FeignClient("rates")
public interface RateConversionClient {

    @RequestMapping(value = "/convert?amount={amount}&amountCurrency={amountCurrency}&toCurrency={toCurrency}",
                    method = RequestMethod.GET, consumes = "application/json")
    RateConversion convert(@PathVariable("amount") BigDecimal amount,
                           @PathVariable("amountCurrency") String amountCurrency,
                           @PathVariable("toCurrency") String toCurrency);

    class RateConversion {
        private Date asOf;
        private String fromCurrency;
        private String toCurrency;
        private BigDecimal amountInFromCurrency;
        private BigDecimal amountInToCurrency;

        public RateConversion() {
        }

        public RateConversion(Date asOf, String fromCurrency, String toCurrency, BigDecimal amountInFromCurrency,
                              BigDecimal amountInToCurrency) {
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
    }


}
