package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.Transaction;
import io.yac.budget.domain.TransactionType;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.PaymentMeanRepository;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class TransactionConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        Transaction entity = prototypeValidTransaction().id(1L).build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_amount_cents_maps_to_entity_amount_cents() {
        Transaction entity = prototypeValidTransaction().amountCents(12439).build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getAmountCents(), is(12439));
    }

    @Test
    public void resource_currency_maps_to_entity_currency() {
        Transaction entity = prototypeValidTransaction().currency("known currency").build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is("known currency"));
    }


    @Test
    public void resource_description_maps_to_entity_description() {
        Transaction entity = prototypeValidTransaction().description("some description").build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getDescription(), is("some description"));
    }

    @Test
    public void resource_date_maps_to_entity_date() {
        final Date knownDate = new Date();
        Transaction entity = prototypeValidTransaction().date(knownDate).build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getDate(), is(knownDate));
    }

    @Test
    public void resource_is_confirmed_maps_to_entity_is_confirmed() {
        Transaction entity = prototypeValidTransaction().isConfirmed(true).build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getConfirmed(), is(true));
    }

    @Test
    public void resourceBank_account_is_a_bank_account_resource_with_id_mapping_to_entity_bank_account_id() {
        Transaction entity = prototypeValidTransaction().bankAccount(BankAccount.builder().id(1L).build()).build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getBankAccount().getId(), is(1L));
    }

    @Test
    public void resourcePaymentMean_is_a_payment_mean_resource_with_id_mapping_to_entity_payment_mean_id() {
        Transaction entity = prototypeValidTransaction().paymentMean(PaymentMean.builder().id(1L).build()).build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getPaymentMean().getId(), is(1L));
    }

    @Test
    public void resource_type_maps_to_entity_type_external_name() {
        Transaction entity = prototypeValidTransaction().type(TransactionType.EXPENSE).build();

        TransactionConverter converter = new TransactionConverter();
        TransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getType(), is(TransactionType.EXPENSE.getExternalName()));
    }

    @Test
    public void entity_id_maps_to_resource_id() {
        TransactionResource resource = TransactionResource.builder().id(1L).build();

        TransactionConverter converter = new TransactionConverter();
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getId(), is(1L));
    }

    @Test
    public void entity_amount_cents_maps_to_resource_amount_cents() {
        TransactionResource resource = TransactionResource.builder().amountCents(199483).build();

        TransactionConverter converter = new TransactionConverter();
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getAmountCents(), is(199483));
    }

    @Test
    public void entity_currency_maps_to_resource_currency() {
        TransactionResource resource = TransactionResource.builder().currency("Known currency").build();

        TransactionConverter converter = new TransactionConverter();
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getCurrency(), is("Known currency"));
    }

    @Test
    public void entity_description_maps_to_resource_description() {
        TransactionResource resource = TransactionResource.builder().description("Known description").build();

        TransactionConverter converter = new TransactionConverter();
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getDescription(), is("Known description"));
    }

    @Test
    public void entity_type_maps_to_resource_transaction_type_derived_from_external_name() {
        TransactionResource resource =
                TransactionResource.builder().type(TransactionType.INCOME.getExternalName()).build();

        TransactionConverter converter = new TransactionConverter();
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getType(), is(TransactionType.INCOME));
    }

    @Test
    public void entity_date_maps_to_resource_date() {
        final Date knownDate = new Date();
        TransactionResource resource = TransactionResource.builder().date(knownDate).build();

        TransactionConverter converter = new TransactionConverter();
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getDate(), is(knownDate));
    }

    @Test
    public void entity_is_confirmed_maps_to_resource_is_confirmed() {
        TransactionResource resource = TransactionResource.builder().isConfirmed(true).build();

        TransactionConverter converter = new TransactionConverter();
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.isConfirmed(), is(true));
    }

    @Test
    public void entity_bank_account_maps_to_bank_account_entity_with_matching_id() {
        TransactionResource resource =
                TransactionResource.builder().bankAccount(BankAccountResource.builder().id(1L).build()).build();

        BankAccount bankAccountFromRepository = BankAccount.builder().id(1L).build();

        BankAccountRepository dummyBankAccountRepository = mock(BankAccountRepository.class);
        when(dummyBankAccountRepository.findOne(1L)).thenReturn(bankAccountFromRepository);

        TransactionConverter converter = new TransactionConverter(dummyBankAccountRepository, null);
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getBankAccount(), is(bankAccountFromRepository));
    }

    @Test
    public void entity_bank_account_is_null_if_resource_bank_account_is_null() {
        TransactionResource resource =
                TransactionResource.builder().bankAccount(null).build();

        TransactionConverter converter = new TransactionConverter(null, null);
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getBankAccount(), is(nullValue()));
    }


    @Test
    public void entity_payment_mean_maps_to_payment_mean_entity_with_matching_id() {
        TransactionResource resource =
                TransactionResource.builder().paymentMean(PaymentMeanResource.builder().id(1L).build()).build();

        PaymentMean paymentMeanFromRepository = PaymentMean.builder().id(1L).build();

        PaymentMeanRepository dummyPaymentMeanRepository = mock(PaymentMeanRepository.class);
        when(dummyPaymentMeanRepository.findOne(1L)).thenReturn(paymentMeanFromRepository);

        TransactionConverter converter = new TransactionConverter(null, dummyPaymentMeanRepository);
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getPaymentMean(), is(paymentMeanFromRepository));
    }

    @Test
    public void entity_payment_mean_is_null_if_resource_payment_mean_is_null() {
        TransactionResource resource =
                TransactionResource.builder().paymentMean(null).build();

        TransactionConverter converter = new TransactionConverter(null, null);
        Transaction entity = converter.convertToEntity(resource);
        assertThat(entity.getPaymentMean(), is(nullValue()));
    }

    private Transaction.Builder prototypeValidTransaction() {
        final TransactionType anyType = TransactionType.EXPENSE;
        return Transaction.builder().type(anyType).bankAccount(BankAccount.builder().build())
                .paymentMean(PaymentMean.builder().build());
    }

}