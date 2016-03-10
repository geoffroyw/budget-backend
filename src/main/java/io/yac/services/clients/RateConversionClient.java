package io.yac.services.clients;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by geoffroy on 10/03/2016.
 */
@FeignClient("rates")
public interface RateConversionClient {
    /**
     * @RequestMapping(value = "/convert")
     * public RateConversionResponse convert(@PathParam(value = "amount") BigDecimal amount,
     * @PathParam(value = "amountCurrency") String amountCurrency,
     * @PathParam(value = "toCurrency") String toCurrency)
     */
    @RequestLine("GET /convert?amount={amount}&amountCurrency={amountCurrency}&toCurrency={toCurrency}")
    RateConversion convert(@Param("amount") BigDecimal amount, @Param("amountCurrency") String amountCurrency,
                           @Param("toCurrency") String toCurrency);

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
