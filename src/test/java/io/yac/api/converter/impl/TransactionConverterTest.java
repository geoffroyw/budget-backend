package io.yac.api.converter.impl;

import io.yac.transaction.api.TransactionResource;
import io.yac.categories.domain.Category;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.core.domain.*;
import io.yac.transaction.api.converter.TransactionConverter;
import io.yac.transaction.domain.Transaction;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.categories.repository.CategoryRepository;
import io.yac.paymentmean.domain.PaymentMean;
import io.yac.paymentmean.repository.PaymentMeanRepository;
import io.yac.transaction.repository.TransactionRepository;
import io.yac.services.clients.rate.RateConversionClient;
import io.yac.services.clients.rate.RateConversionClient.RateConversionResponse;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class TransactionConverterTest {

    private RateConversionClient dummy_rateConversionClient;

    @Before
    public void setUp() {
        dummy_rateConversionClient = mock(RateConversionClient.class);
        when(dummy_rateConversionClient.convert(anyObject(), anyString(), anyString()))
                .thenReturn(new RateConversionResponse(null, null, null, null, new BigDecimal(10)));
    }


    @Test
    public void resource_id_maps_to_entity_id() {
        Transaction entity = prototypeValidTransaction().id(1L).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_amount_cents_maps_to_entity_amount_cents() {
        Transaction entity = prototypeValidTransaction().amountCents(12439).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getAmountCents(), is(12439));
    }

    @Test
    public void resource_currency_maps_to_entity_currency_external_name() {
        SupportedCurrency knownCurrency = SupportedCurrency.EUR;
        Transaction entity = prototypeValidTransaction().currency(knownCurrency).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is(SupportedCurrency.EUR.getExternalName()));
    }


    @Test
    public void resource_settlement_amount_cents_maps_to_entity_settlement_amount_cents_if_not_null() {
        Transaction entity =
                prototypeValidTransaction().currency(SupportedCurrency.EUR).settlementAmountCents(12439).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getSettlementAmountCents(), is(12439));
    }

    @Test
    public void resource_settlement_currency_maps_to_entity_settlement_currency_external_name_if_not_null() {
        SupportedCurrency knownCurrency = SupportedCurrency.EUR;
        Transaction entity =
                prototypeValidTransaction().currency(SupportedCurrency.IDR).settlementCurrency(knownCurrency).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null,
                dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getSettlementCurrency(), is(SupportedCurrency.EUR.getExternalName()));
    }

    @Test
    public void resource_isSettlementAmountIndicative_is_true_if_entity_settlement_amount_is_null() {
        Transaction entity = prototypeValidTransaction().settlementAmountCents(null).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.isSettlementAmountIndicative(), is(true));
    }

    @Test
    public void resource_isSettlementAmountIndicative_is_false_if_entity_settlement_amount_is_not_null() {
        Transaction entity = prototypeValidTransaction().settlementAmountCents(193280).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.isSettlementAmountIndicative(), is(false));
    }

    @Test
    public void resource_settlement_amount_cents_maps_to_rate_client_converted_amount_if_entity_settlement_amount_is_null() {
        Transaction entity = prototypeValidTransaction().currency(SupportedCurrency.EUR).amountCents(3827)
                .settlementAmountCents(null).build();

        RateConversionClient dummy_rateConversionClient = mock(RateConversionClient.class);
        when(dummy_rateConversionClient.convert(anyObject(), anyString(), anyString()))
                .thenReturn(new RateConversionResponse(null, null, null, new BigDecimal(3827 / 100),
                        new BigDecimal("12.43")));

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getSettlementAmountCents(), is(1243));
    }

    @Test
    public void resource_settlement_amount_cents_is_null_if_rate_client_response_is_null() {
        Transaction entity = prototypeValidTransaction().currency(SupportedCurrency.EUR).amountCents(3827)
                .settlementAmountCents(null).build();

        RateConversionClient dummy_rateConversionClient = mock(RateConversionClient.class);
        when(dummy_rateConversionClient.convert(anyObject(), anyString(), anyString()))
                .thenReturn(null);

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getSettlementAmountCents(), is(nullValue()));
    }

    @Test
    public void resource_settlement_amount_cents_maps_to_entity_amount_cents_if_amount_currency_is_payment_mean_currency() {
        SupportedCurrency knownCurrency = SupportedCurrency.EUR;
        Transaction entity =
                prototypeValidTransaction().amountCents(104394)
                        .paymentMean(PaymentMean.builder().currency(knownCurrency).build())
                        .currency(knownCurrency).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getSettlementAmountCents(), is(104394));
    }

    @Test
    public void resource_settlement_currency_maps_to_entity_amount_currency_if_amount_currency_is_payment_mean_currency() {
        SupportedCurrency knownCurrency = SupportedCurrency.EUR;
        Transaction entity =
                prototypeValidTransaction().amountCents(104394).currency(knownCurrency)
                        .paymentMean(PaymentMean.builder().currency(knownCurrency).build())
                        .settlementCurrency(null).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getSettlementCurrency(), is(knownCurrency.getExternalName()));
    }

    @Test
    public void resource_settlement_amount_cents_maps_to_payment_mean_currency_if_entity_settlement_currency_is_null() {
        SupportedCurrency knownCurrency = SupportedCurrency.EUR;
        Transaction entity =
                prototypeValidTransaction().paymentMean(PaymentMean.builder().currency(knownCurrency).build())
                        .settlementCurrency(null).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getSettlementCurrency(), is(SupportedCurrency.EUR.getExternalName()));
    }

    @Test
    public void resource_description_maps_to_entity_description() {
        Transaction entity = prototypeValidTransaction().description("some description").build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getDescription(), is("some description"));
    }

    @Test
    public void resource_date_maps_to_entity_date() {
        final Date knownDate = new Date();
        Transaction entity = prototypeValidTransaction().date(knownDate).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getDate(), is(knownDate));
    }

    @Test
    public void resource_is_confirmed_maps_to_entity_is_confirmed() {
        Transaction entity = prototypeValidTransaction().isConfirmed(true).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getIsConfirmed(), is(true));
    }

    @Test
    public void resourceBank_account_is_a_bank_account_resource_with_id_mapping_to_entity_bank_account_id() {
        Transaction entity = prototypeValidTransaction().bankAccount(BankAccount.builder().id(1L).build()).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getBankAccount(), is(1L));
    }

    @Test
    public void resourcePaymentMean_is_a_payment_mean_resource_with_id_mapping_to_entity_payment_mean_id() {
        Transaction entity = prototypeValidTransaction()
                .paymentMean(PaymentMean.builder().id(1L).currency(SupportedCurrency.USD).build()).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getPaymentMean(), is(1L));
    }

    @Test
    public void resource_categories_maps_to_a_list_of_categories_resources_with_id_from_entity_transaction_ids() {
        Transaction entity =
                prototypeValidTransaction().categories(Collections.singletonList(Category.builder().id(1L).build()))
                        .build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getCategories().get(0), is(1L));
    }

    @Test
    public void resource_categories_is_null_if_entity_categories_is_null() {
        Transaction entity = prototypeValidTransaction().categories(null).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        TransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getCategories(), is(nullValue()));
    }


    @Test
    public void entity_amount_cents_maps_to_resource_amount_cents() {
        TransactionResource resource = TransactionResource.builder().amountCents(199483).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getAmountCents(), is(199483));
    }

    @Test
    public void entity_currency_maps_to_resource_currency() {
        TransactionResource resource =
                TransactionResource.builder().currency(SupportedCurrency.EUR.getExternalName()).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getCurrency(), is(SupportedCurrency.EUR));
    }

    @Test
    public void entity_settlement_amount_cents_maps_to_resource_settlement_amount_cents() {
        TransactionResource resource = TransactionResource.builder().settlementAmountCents(199483).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getSettlementAmountCents(), is(199483));
    }

    @Test
    public void entity_settlement_currency_maps_to_resource_settlement_currency() {
        TransactionResource resource =
                TransactionResource.builder().settlementCurrency(SupportedCurrency.EUR.getExternalName()).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getSettlementCurrency(), is(SupportedCurrency.EUR));
    }


    @Test
    public void entity_description_maps_to_resource_description() {
        TransactionResource resource = TransactionResource.builder().description("Known description").build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getDescription(), is("Known description"));
    }

    @Test
    public void entity_date_maps_to_resource_date() {
        final Date knownDate = new Date();
        TransactionResource resource = TransactionResource.builder().date(knownDate).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getDate(), is(knownDate));
    }

    @Test
    public void entity_is_confirmed_maps_to_resource_is_confirmed() {
        TransactionResource resource = TransactionResource.builder().isConfirmed(true).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, dummy_rateConversionClient);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.isConfirmed(), is(true));
    }

    @Test
    public void entity_bank_account_maps_to_bank_account_entity_with_matching_id() {
        TransactionResource resource =
                TransactionResource.builder().bankAccount(1L).build();

        BankAccount bankAccountFromRepository = BankAccount.builder().id(1L).build();

        BankAccountRepository dummyBankAccountRepository = mock(BankAccountRepository.class);
        when(dummyBankAccountRepository.findOne(1L)).thenReturn(bankAccountFromRepository);

        TransactionConverter converter = new TransactionConverter(dummyBankAccountRepository, null, null, null, null);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getBankAccount(), is(bankAccountFromRepository));
    }

    @Test
    public void entity_bank_account_is_null_if_resource_bank_account_is_null() {
        TransactionResource resource =
                TransactionResource.builder().bankAccount(null).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, null);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getBankAccount(), is(nullValue()));
    }

    @Test
    public void entity_payment_mean_maps_to_payment_mean_entity_with_matching_id() {
        TransactionResource resource =
                TransactionResource.builder().paymentMean(1L).build();

        PaymentMean paymentMeanFromRepository = PaymentMean.builder().id(1L).build();

        PaymentMeanRepository dummyPaymentMeanRepository = mock(PaymentMeanRepository.class);
        when(dummyPaymentMeanRepository.findOne(1L)).thenReturn(paymentMeanFromRepository);

        TransactionConverter converter = new TransactionConverter(null, dummyPaymentMeanRepository, null, null, null);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getPaymentMean(), is(paymentMeanFromRepository));
    }

    @Test
    public void entity_payment_mean_is_null_if_resource_payment_mean_is_null() {
        TransactionResource resource =
                TransactionResource.builder().paymentMean(null).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, null);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getPaymentMean(), is(nullValue()));
    }

    @Test
    public void entity_categories_maps_to_list_of_category_entity_with_matching_ids() {
        TransactionResource resource =
                TransactionResource.builder()
                        .categories(Collections.singletonList(1L)).build();

        Category categoryFromRepository = Category.builder().id(1L).build();

        CategoryRepository dummyCategoryRepository = mock(CategoryRepository.class);
        when(dummyCategoryRepository.findAll(anyList()))
                .thenReturn(Collections.singletonList(categoryFromRepository));

        TransactionConverter converter = new TransactionConverter(null, null, dummyCategoryRepository, null, null);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getCategories().get(0), is(categoryFromRepository));
    }

    @Test
    public void entity_categories_is_null_if_resource_categories_mean_is_null() {
        TransactionResource resource =
                TransactionResource.builder().categories(null).build();

        TransactionConverter converter = new TransactionConverter(null, null, null, null, null);
        Transaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getCategories(), is(nullValue()));
    }

    @Test
    public void convertToEntity_updates_the_entity_if_a_non_null_id_is_passed_as_parameter() {
        TransactionResource resource_of_existing_entity =
                TransactionResource.builder().currency("CHF").isConfirmed(false).build();
        Transaction transactionFromDb = Transaction.builder().id(1L).build();

        TransactionRepository dummyTransactionRepository = mock(TransactionRepository.class);
        when(dummyTransactionRepository.findOne(1L)).thenReturn(transactionFromDb);

        TransactionConverter converter = new TransactionConverter(null, null, null, dummyTransactionRepository, null);
        Transaction entity = converter.convertToEntity(resource_of_existing_entity, 1L);
        assertThat(entity, is(transactionFromDb));
    }

    private Transaction.Builder prototypeValidTransaction() {
        return Transaction.builder().bankAccount(BankAccount.builder().build()).currency(SupportedCurrency.USD)
                .amountCents(1)
                .paymentMean(PaymentMean.builder().currency(SupportedCurrency.USD).build())
                .categories(Collections.singletonList(Category.builder().build()));
    }

}