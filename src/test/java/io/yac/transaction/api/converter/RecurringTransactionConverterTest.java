package io.yac.transaction.api.converter;

import io.yac.transaction.api.RecurringTransactionResource;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.categories.domain.Category;
import io.yac.paymentmean.domain.PaymentMean;
import io.yac.common.domain.SupportedCurrency;
import io.yac.transaction.domain.RecurringTransaction;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.categories.repository.CategoryRepository;
import io.yac.paymentmean.repository.PaymentMeanRepository;
import io.yac.common.scheduler.expression.TemporalExpression.TemporalExpressionType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 19/02/2016.
 */
public class RecurringTransactionConverterTest {

    @Test
    public void resource_id_maps_to_service_response_id() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().id(1L).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_amount_cents_maps_to_service_response_amount_cents() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().amountCents(12439).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getAmountCents(), is(12439));
    }

    @Test
    public void resource_is_active_maps_to_service_response_is_active() {
        RecurringTransaction entity = prototypeValidRecurringTransaction().isActive(true).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getIsActive(), is(true));
    }

    @Test
    public void resource_currency_maps_to_service_response_currency_external_name() {
        RecurringTransaction entity =
                prototypeValidRecurringTransaction().currency(SupportedCurrency.EUR).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is("EUR"));
    }


    @Test
    public void resource_description_maps_to_service_response_description() {
        RecurringTransaction entity =
                prototypeValidRecurringTransaction().description("some description").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getDescription(), is("some description"));
    }

    @Test
    public void resource_recurring_type_maps_to_service_response_temporal_expression_type() {
        RecurringTransaction entity =
                prototypeValidRecurringTransaction().temporalExpressionType(
                        TemporalExpressionType.DAILY).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getRecurringType(), is("Daily"));
    }

    @Test
    public void resource_last_run_one_maps_to_service_response_last_run_on() {
        Date known_date = new Date();
        RecurringTransaction entity =
                prototypeValidRecurringTransaction().lastRunOn(known_date).build();

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

        assertThat(resource.getBankAccount(), is(1L));
    }

    @Test
    public void resourcePaymentMean_is_a_payment_mean_resource_with_id_mapping_to_entity_payment_mean_id() {
        RecurringTransaction entity =
                prototypeValidRecurringTransaction().paymentMean(PaymentMean.builder().id(1L).build()).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getPaymentMean(), is(1L));
    }

    @Test
    public void resource_categories_maps_to_a_list_of_categories_resources_with_id_from_entity_recurringTransaction_ids() {
        RecurringTransaction entity =
                prototypeValidRecurringTransaction()
                        .categories(Collections.singletonList(Category.builder().id(1L).build())).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getCategories().get(0), is(1L));
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
        RecurringTransaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getAmountCents(), is(199483));
    }

    @Test
    public void entity_currency_maps_to_resource_currency() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().currency("EUR").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getCurrency(), is(SupportedCurrency.EUR));
    }

    @Test
    public void entity_description_maps_to_resource_description() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().description("Known description").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getDescription(), is("Known description"));
    }

    @Test
    public void entity_is_active_maps_to_resource_is_active() {
        RecurringTransactionResource resource = RecurringTransactionResource.builder().isActive(true).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.isActive(), is(true));
    }

    @Test
    public void entity_recurring_transaction_type_maps_to_resource_recurring_type_by_external_name() {
        RecurringTransactionResource resource = RecurringTransactionResource.builder().recurringType(
                TemporalExpressionType.MONTHLY.getExternalName()).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getTemporalExpressionType(), is(TemporalExpressionType.MONTHLY));
    }

    @Test
    public void entity_bank_account_maps_to_bank_account_entity_with_matching_id() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().bankAccount(1L)
                        .build();

        BankAccountRepository dummy_bankAccountRepository = mock(BankAccountRepository.class);
        final BankAccount bankAccount = BankAccount.builder().id(1L).build();
        when(dummy_bankAccountRepository.findOne(1L)).thenReturn(bankAccount);


        RecurringTransactionConverter converter =
                new RecurringTransactionConverter(null, null, null, dummy_bankAccountRepository);
        RecurringTransaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getBankAccount(), is(bankAccount));
    }

    @Test
    public void entity_payment_mean_maps_to_payment_mean_entity_with_matching_id() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().paymentMean(1L)
                        .build();

        PaymentMeanRepository dummy_paymentMeanRepository = mock(PaymentMeanRepository.class);
        final PaymentMean paymentMean = PaymentMean.builder().id(1L).build();
        when(dummy_paymentMeanRepository.findOne(1L)).thenReturn(paymentMean);

        RecurringTransactionConverter converter =
                new RecurringTransactionConverter(null, null, dummy_paymentMeanRepository, null);
        RecurringTransaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getPaymentMean(), is(paymentMean));
    }

    @Test
    public void entity_categories_maps_to_list_of_category_entity_with_matching_ids() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder()
                        .categories(Collections.singletonList(1L)).build();

        CategoryRepository dummy_categoryRepository = mock(CategoryRepository.class);
        final Category category = Category.builder().id(1L).build();
        final List<Category> categoryList = Collections.singletonList(category);
        when(dummy_categoryRepository.findAll(Collections.singletonList(1L))).thenReturn(
                categoryList);

        RecurringTransactionConverter converter =
                new RecurringTransactionConverter(null, dummy_categoryRepository, null, null);
        RecurringTransaction entity = converter.convertToEntity(resource, null);
        assertThat(entity.getCategories(), is(categoryList));
    }

    private RecurringTransaction.Builder prototypeValidRecurringTransaction() {
        return RecurringTransaction.builder().categories(new ArrayList<>()).currency(SupportedCurrency.BGN)
                .temporalExpressionType(TemporalExpressionType.YEARLY)
                .bankAccount(BankAccount.builder().build()).paymentMean(PaymentMean.builder().build());
    }
}