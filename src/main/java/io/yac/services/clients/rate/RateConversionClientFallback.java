package io.yac.services.clients.rate;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by geoffroy on 22/03/2016.
 */
@Component
public class RateConversionClientFallback implements RateConversionClient {
    @Override
    public RateConversionResponse convert(BigDecimal amount, String amountCurrency, String toCurrency) {
        return null;
    }
}
