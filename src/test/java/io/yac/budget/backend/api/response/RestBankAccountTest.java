package io.yac.budget.backend.api.response;

import io.yac.budget.backend.domain.entity.BankAccount;
import org.junit.Test;

import java.util.Currency;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by geoffroy on 24/01/2016.
 */
public class RestBankAccountTest {

    @Test
    public void id_maps_to_bank_account_id() {
        Currency any_currency = Currency.getInstance("USD");
        BankAccount bankAccount = BankAccount.builder().id(1L).currency(any_currency).build();

        assertThat(new RestBankAccount(bankAccount).getId(), is(1L));
    }

    @Test
    public void currency_code_maps_to_bank_account_currency_currency_code() {
        BankAccount bankAccount = BankAccount.builder().currency(Currency.getInstance("EUR")).build();

        assertThat(new RestBankAccount(bankAccount).getCurrencyCode(), is("EUR"));
    }

    @Test
    public void currency_maps_to_currency_from_rest_object_currency_code() {
        RestBankAccount restBankAccount = RestBankAccount.builder().currencyCode("USD").build();

        assertThat(restBankAccount.toBankAccount().getCurrency(), is(Currency.getInstance("USD")));
    }

    @Test
    public void id_maps_to_id_from_rest_object() {
        String any_currency = "EUR";
        RestBankAccount restBankAccount = RestBankAccount.builder().currencyCode(any_currency).id(1L).build();

        assertThat(restBankAccount.toBankAccount().getId(), is(1L));
    }

}