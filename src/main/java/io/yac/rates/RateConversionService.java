package io.yac.rates;

import com.google.common.annotations.VisibleForTesting;
import io.yac.rates.domain.CurrencyRate;
import io.yac.rates.domain.ExchangeRate;
import io.yac.rates.exceptions.NoRateException;
import io.yac.rates.exceptions.UnknownCurrencyException;
import io.yac.rates.repository.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by geoffroy on 18/02/2016.
 */
@Service
public class RateConversionService {

    public static final String EUR = "EUR";
    private static final Logger LOG = LoggerFactory.getLogger(RateConversionService.class);
    @Autowired
    CurrencyRateRepository rateRepository;

    public RateConversionService() {
    }

    @VisibleForTesting RateConversionService(CurrencyRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public RateConversionResponse convert(final BigDecimal amount,
                                          final String amountCurrency,
                                          final String toCurrency)
            throws UnknownCurrencyException, NoRateException {
        LOG.info("Currency conversion request received " + amount + " " + amountCurrency + " " + toCurrency);
        RateConversionResponse response = processRequest(amount, amountCurrency, toCurrency);
        LOG.info("Currency conversion response " + response + "  for request" + amount + " " + amountCurrency + " " +
                toCurrency);
        return response;

    }


    private RateConversionResponse processRequest(final BigDecimal amountInFromCurrency, final String fromCurrency,
                                                  final String toCurrency)
            throws UnknownCurrencyException, NoRateException {


        CurrencyRate rateOfFromCurrency = rateRepository.findByCurrency(fromCurrency);
        CurrencyRate rateOfToCurrency = rateRepository.findByCurrency(toCurrency);

        if (rateOfFromCurrency == null && !EUR.equalsIgnoreCase(fromCurrency)) {
            throw new UnknownCurrencyException("Currency " + fromCurrency + " is not supported");
        }
        if (rateOfToCurrency == null && !EUR.equalsIgnoreCase(toCurrency)) {
            throw new UnknownCurrencyException("Currency " + toCurrency + " is not supported");
        }

        BigDecimal convertedAmount = amountInFromCurrency;
        Date asOf;
        if (EUR.equalsIgnoreCase(fromCurrency)) {
            ExchangeRate rate = rateOfToCurrency.getLatestRate();
            convertedAmount = convertedAmount.multiply(rate.getAmount());
            asOf = rate.getDate();
        } else {
            if (EUR.equalsIgnoreCase(toCurrency)) {
                ExchangeRate rate = rateOfFromCurrency.getLatestRate();
                convertedAmount = convertedAmount.divide(rate.getAmount(), 2);
                asOf = rate.getDate();
            } else {
                asOf = rateOfFromCurrency.getLatestRate().getDate();
                convertedAmount = convertedAmount.multiply(rateOfToCurrency.getLatestRate().getAmount())
                        .divide(rateOfFromCurrency.getLatestRate().getAmount(), 2);
            }
        }

        return new RateConversionResponse(asOf, fromCurrency, toCurrency, amountInFromCurrency, convertedAmount);
    }
}
