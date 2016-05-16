package io.yac.rates.domain;

import io.yac.rates.exceptions.NoRateException;
import org.junit.Test;

import java.util.ArrayList;


/**
 * Created by geoffroy on 18/02/2016.
 */
public class CurrencyRateTest {

    @Test(expected = NoRateException.class)
    public void latestRates_throws_NoRateException_if_rates_is_null() throws NoRateException {
        CurrencyRate currencyRate = CurrencyRate.builder().rates(null).build();

        currencyRate.getLatestRate();
    }

    @Test(expected = NoRateException.class)
    public void latestRates_throws_NoRateException_if_rates_is_empty_list() throws NoRateException {
        CurrencyRate currencyRate = CurrencyRate.builder().rates(new ArrayList<>()).build();

        currencyRate.getLatestRate();
    }

}