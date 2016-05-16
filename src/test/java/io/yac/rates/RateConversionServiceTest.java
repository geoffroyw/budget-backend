package io.yac.rates;

import io.yac.rates.domain.CurrencyRate;
import io.yac.rates.domain.ExchangeRate;
import io.yac.rates.exceptions.NoRateException;
import io.yac.rates.exceptions.UnknownCurrencyException;
import io.yac.rates.repository.CurrencyRateRepository;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 18/02/2016.
 */
public class RateConversionServiceTest {

    @Test(expected = UnknownCurrencyException.class)
    public void processResponse_throws_unknown_currency_if_one_of_the_two_currency_is_not_supported()
            throws UnknownCurrencyException, NoRateException {

        CurrencyRateRepository dummyCurrencyRateRepository = mock(CurrencyRateRepository.class);
        when(dummyCurrencyRateRepository.findByCurrency(anyString())).thenReturn(null);

        RateConversionService rateConversionService = new RateConversionService(dummyCurrencyRateRepository);

        rateConversionService.convert(null, "any", "other");

    }


    @Test
    public void response_fromCurrency_is_amountCurrency_from_request()
            throws UnknownCurrencyException, NoRateException {

        Date date =
                Date.from(Instant.from(LocalDate.of(2011, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        ExchangeRate rate = ExchangeRate.builder().amount(BigDecimal.ONE).date(date).build();


        CurrencyRateRepository dummyCurrencyRateRepository = mock(CurrencyRateRepository.class);
        when(dummyCurrencyRateRepository.findByCurrency("USD")).thenReturn(
                CurrencyRate.builder().currency("USD").rates(Collections.singletonList(rate)).build());

        RateConversionService rateConversionService = new RateConversionService(dummyCurrencyRateRepository);

        assertThat(rateConversionService.convert(new BigDecimal("10.34"), "EUR", "USD").getFromCurrency(), is("EUR"));
    }

    @Test
    public void response_toCurrency_is_request_toCurrency() throws UnknownCurrencyException, NoRateException {

        Date date =
                Date.from(Instant.from(LocalDate.of(2011, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        ExchangeRate rate = ExchangeRate.builder().amount(BigDecimal.ONE).date(date).build();


        CurrencyRateRepository dummyCurrencyRateRepository = mock(CurrencyRateRepository.class);
        when(dummyCurrencyRateRepository.findByCurrency("USD")).thenReturn(
                CurrencyRate.builder().currency("USD").rates(Collections.singletonList(rate)).build());

        RateConversionService rateConversionService = new RateConversionService(dummyCurrencyRateRepository);

        assertThat(rateConversionService.convert(new BigDecimal("10.34"), "EUR", "USD").getToCurrency(),
                is("USD"));
    }


    @Test
    public void response_asOf_is_exchange_rates_date() throws UnknownCurrencyException, NoRateException {

        Date exchangeRateDate =
                Date.from(Instant.from(LocalDate.of(2011, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        ExchangeRate rate = ExchangeRate.builder().amount(BigDecimal.TEN).date(exchangeRateDate).build();


        CurrencyRateRepository dummyCurrencyRateRepository = mock(CurrencyRateRepository.class);
        when(dummyCurrencyRateRepository.findByCurrency("USD")).thenReturn(
                CurrencyRate.builder().currency("USD").rates(Collections.singletonList(rate)).build());

        RateConversionService rateConversionService = new RateConversionService(dummyCurrencyRateRepository);

        assertThat(rateConversionService.convert(new BigDecimal("10.34"), "EUR", "USD").getAsOf(),
                is(exchangeRateDate));
    }


    @Test
    public void response_amounts_in_from_currency_is_the_amount_of_the_request()
            throws UnknownCurrencyException, NoRateException {

        Date exchangeRateDate =
                Date.from(Instant.from(LocalDate.of(2011, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        ExchangeRate rate = ExchangeRate.builder().amount(BigDecimal.TEN).date(exchangeRateDate).build();


        CurrencyRateRepository dummyCurrencyRateRepository = mock(CurrencyRateRepository.class);
        when(dummyCurrencyRateRepository.findByCurrency("USD")).thenReturn(
                CurrencyRate.builder().currency("USD").rates(Collections.singletonList(rate)).build());

        RateConversionService rateConversionService = new RateConversionService(dummyCurrencyRateRepository);

        assertThat(rateConversionService.convert(new BigDecimal("10.34"), "EUR", "USD").getAmountInFromCurrency(),
                is(new BigDecimal("10.34")));
    }

    @Test
    public void response_amountInToCurrency_is_requested_amount_multiplied_by_the_latest_exchange_rates_one_from_repository_if_amount_currency_is_EUR_and_to_currency_is_known()
            throws UnknownCurrencyException, NoRateException {

        Date latestDate =
                Date.from(Instant.from(LocalDate.of(2016, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Date oldestDate =
                Date.from(Instant.from(LocalDate.of(2011, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        ExchangeRate latestRate = ExchangeRate.builder().amount(BigDecimal.TEN).date(latestDate).build();
        ExchangeRate oldestRate = ExchangeRate.builder().amount(BigDecimal.ONE).date(oldestDate).build();


        CurrencyRateRepository dummyCurrencyRateRepository = mock(CurrencyRateRepository.class);
        when(dummyCurrencyRateRepository.findByCurrency("USD")).thenReturn(
                CurrencyRate.builder().currency("USD").rates(Arrays.asList(latestRate, oldestRate)).build());

        RateConversionService rateConversionService = new RateConversionService(dummyCurrencyRateRepository);

        assertThat(rateConversionService.convert(new BigDecimal("10.34"), "EUR", "USD").getAmountInToCurrency(),
                is(new BigDecimal("103.40")));
    }

    @Test
    public void response_amountInToCurrency_is_requested_amount_divided_by_the_latest_exchange_rates_one_from_repository_if_amount_currency_is_known_and_to_currency_is_EUR()
            throws UnknownCurrencyException, NoRateException {

        Date latestDate =
                Date.from(Instant.from(LocalDate.of(2016, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Date oldestDate =
                Date.from(Instant.from(LocalDate.of(2011, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        ExchangeRate latestRate = ExchangeRate.builder().amount(BigDecimal.TEN).date(latestDate).build();
        ExchangeRate oldestRate = ExchangeRate.builder().amount(BigDecimal.ONE).date(oldestDate).build();


        CurrencyRateRepository dummyCurrencyRateRepository = mock(CurrencyRateRepository.class);
        when(dummyCurrencyRateRepository.findByCurrency("USD")).thenReturn(
                CurrencyRate.builder().currency("USD").rates(Arrays.asList(latestRate, oldestRate)).build());

        RateConversionService rateConversionService = new RateConversionService(dummyCurrencyRateRepository);

        assertThat(rateConversionService.convert(new BigDecimal("10.34"), "USD", "EUR").getAmountInToCurrency(),
                is(new BigDecimal("1.04"))); //rounding at 2 decimals
    }

    @Test
    public void response_amountInToCurrency_handles_conversion_of_two_currencies_by_using_EUR_as_intermediate_currency()
            throws UnknownCurrencyException, NoRateException {

        Date exchangeRateDate =
                Date.from(Instant.from(LocalDate.of(2016, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        ExchangeRate chfRate = ExchangeRate.builder().amount(new BigDecimal("2")).date(exchangeRateDate).build();
        ExchangeRate usdRate = ExchangeRate.builder().amount(new BigDecimal("7")).date(exchangeRateDate).build();


        CurrencyRateRepository dummyCurrencyRateRepository = mock(CurrencyRateRepository.class);
        when(dummyCurrencyRateRepository.findByCurrency("USD")).thenReturn(
                CurrencyRate.builder().currency("USD").rates(Collections.singletonList(usdRate)).build());
        when(dummyCurrencyRateRepository.findByCurrency("CHF")).thenReturn(
                CurrencyRate.builder().currency("CHF").rates(Collections.singletonList(chfRate)).build());

        RateConversionService rateConversionService = new RateConversionService(dummyCurrencyRateRepository);

        assertThat(rateConversionService.convert(new BigDecimal("18.15"), "CHF", "USD").getAmountInToCurrency(),
                is(new BigDecimal("63.53"))); //Rounding at 2 decimals
    }
}