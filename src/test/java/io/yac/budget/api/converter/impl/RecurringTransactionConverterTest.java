package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.Category;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.CategoryRepository;
import io.yac.budget.repository.PaymentMeanRepository;
import io.yac.budget.repository.RecurringTransactionRepository;
import io.yac.budget.schedule.temporal.expression.TemporalExpression;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 19/02/2016.
 */
public class RecurringTransactionConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().id(1L).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_amount_cents_maps_to_entity_amount_cents() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().amountCents(12439).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getAmountCents(), is(12439));
    }

    @Test
    public void resource_is_active_maps_to_entity_is_active() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().isActive(true).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.isActive(), is(true));
    }

    @Test
    public void resource_currency_maps_to_entity_currency() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().currency("known currency").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is("known currency"));
    }


    @Test
    public void resource_description_maps_to_entity_description() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().description("some description").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getDescription(), is("some description"));
    }

    @Test
    public void resource_recurring_type_maps_to_entity_temporal_expression_type() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().temporalExpressionType(
                TemporalExpression.TemporalExpressionType.DAILY).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getRecurringType(), is("DAILY"));
    }

    @Test
    public void resource_last_run_one_maps_to_entity_last_run_on() {
        Date known_date = new Date();
        RecurringTransaction entity = prototypeValidRecurringTransaction().lasRunOn(known_date).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getLastRunOn(), is(known_date));
    }

    @Test
    public void resourceBank_account_is_a_bank_account_resource_with_id_mapping_to_entity_bank_account_id() {
        RecurringTransaction entity =
                prototypeValidRecurringTransaction().bankAccount(BankAccount.builder().id(1L).build()).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getBankAccount().getId(), is(1L));
    }

    @Test
    public void resourcePaymentMean_is_a_payment_mean_resource_with_id_mapping_to_entity_payment_mean_id() {
        RecurringTransaction entity =
                prototypeValidRecurringTransaction().paymentMean(PaymentMean.builder().id(1L).build()).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getPaymentMean().getId(), is(1L));
    }

    @Test
    public void resource_categories_maps_to_a_list_of_categories_resources_with_id_from_entity_recurringTransaction_ids() {
        RecurringTransaction entity =
                prototypeValidRecurringTransaction()
                        .categories(Collections.singletonList(Category.builder().id(1L).build()))
                        .build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getCategories().get(0).getId(), is(1L));
    }

    @Test
    public void resource_categories_is_null_if_entity_categories_is_null() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().categories(null).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getCategories(), is(nullValue()));
    }


    @Test
    public void entity_amount_cents_maps_to_resource_amount_cents() {
        RecurringTransactionResource resource = RecurringTransactionResource.builder().amountCents(199483).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getAmountCents(), is(199483));
    }

    @Test
    public void entity_currency_maps_to_resource_currency() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().currency("Known currency").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getCurrency(), is("Known currency"));
    }

    @Test
    public void entity_description_maps_to_resource_description() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().description("Known description").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getDescription(), is("Known description"));
    }

    @Test
    public void entity_last_run_is_not_modified() {
        final Date knownDate = new Date();
        RecurringTransactionResource resource = RecurringTransactionResource.builder().lastRunOn(knownDate).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getLastRunOn(), is(nullValue()));
    }

    @Test
    public void entity_is_active_maps_to_resource_is_active() {
        RecurringTransactionResource resource = RecurringTransactionResource.builder().isActive(true).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.isActive(), is(true));
    }

    @Test
    public void entity_recurring_transaction_type_maps_to_resource_recurring_type() {
        RecurringTransactionResource resource = RecurringTransactionResource.builder().recurringType("MONTHLY").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getTemporalExpressionType(), is(TemporalExpression.TemporalExpressionType.MONTHLY));
    }

    @Test
    public void entity_bank_account_maps_to_bank_account_entity_with_matching_id() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().bankAccount(BankAccountResource.builder().id(1L).build())
                        .build();

        BankAccount bankAccountFromRepository = BankAccount.builder().id(1L).build();

        BankAccountRepository dummyBankAccountRepository = mock(BankAccountRepository.class);
        when(dummyBankAccountRepository.findOne(1L)).thenReturn(bankAccountFromRepository);

        RecurringTransactionConverter converter =
                new RecurringTransactionConverter(dummyBankAccountRepository, null, null, null);
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getBankAccount(), is(bankAccountFromRepository));
    }

    @Test
    public void entity_bank_account_is_null_if_resource_bank_account_is_null() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().bankAccount(null).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter(null, null, null, null);
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getBankAccount(), is(nullValue()));
    }

    @Test
    public void entity_payment_mean_maps_to_payment_mean_entity_with_matching_id() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().paymentMean(PaymentMeanResource.builder().id(1L).build())
                        .build();

        PaymentMean paymentMeanFromRepository = PaymentMean.builder().id(1L).build();

        PaymentMeanRepository dummyPaymentMeanRepository = mock(PaymentMeanRepository.class);
        when(dummyPaymentMeanRepository.findOne(1L)).thenReturn(paymentMeanFromRepository);

        RecurringTransactionConverter converter =
                new RecurringTransactionConverter(null, dummyPaymentMeanRepository, null, null);
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getPaymentMean(), is(paymentMeanFromRepository));
    }

    @Test
    public void entity_payment_mean_is_null_if_resource_payment_mean_is_null() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().paymentMean(null).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter(null, null, null, null);
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getPaymentMean(), is(nullValue()));
    }

    @Test
    public void entity_categories_maps_to_list_of_category_entity_with_matching_ids() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder()
                        .categories(Collections.singletonList(CategoryResource.builder().id(1L).build())).build();

        Category categoryFromRepository = Category.builder().id(1L).build();

        CategoryRepository dummyCategoryRepository = mock(CategoryRepository.class);
        when(dummyCategoryRepository.findAll(anyList()))
                .thenReturn(Collections.singletonList(categoryFromRepository));

        RecurringTransactionConverter converter =
                new RecurringTransactionConverter(null, null, dummyCategoryRepository, null);
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getCategories().get(0), is(categoryFromRepository));
    }

    @Test
    public void entity_categories_is_null_if_resource_categories_mean_is_null() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().categories(null).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter(null, null, null, null);
        RecurringTransaction entity = converter.convertToEntity(resource);
        assertThat(entity.getCategories(), is(nullValue()));
    }

    @Test
    public void convertToEntity_updates_the_entity_if_the_resource_is_passed_with_an_id() {
        RecurringTransactionResource resource_of_existing_entity =
                RecurringTransactionResource.builder().id(1L).currency("CHF").build();
        RecurringTransaction recurringTransactionFromDb = RecurringTransaction.builder().id(1L).build();

        RecurringTransactionRepository dummyRecurringTransactionRepository = mock(RecurringTransactionRepository.class);
        when(dummyRecurringTransactionRepository.findOne(1L)).thenReturn(recurringTransactionFromDb);

        RecurringTransactionConverter converter =
                new RecurringTransactionConverter(null, null, null, dummyRecurringTransactionRepository);
        RecurringTransaction entity = converter.convertToEntity(resource_of_existing_entity);
        assertThat(entity, is(recurringTransactionFromDb));
    }

    private RecurringTransaction.Builder prototypeValidRecurringTransaction() {
        return RecurringTransaction.builder().bankAccount(BankAccount.builder().build()).temporalExpressionType(
                TemporalExpression.TemporalExpressionType.DAILY)
                .paymentMean(PaymentMean.builder().build())
                .categories(Collections.singletonList(Category.builder().build()));
    }
}