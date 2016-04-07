package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.recurring.transactions.client.RecurringTransactionRequest;
import io.yac.budget.recurring.transactions.client.resources.RecurringTransactionResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by geoffroy on 19/02/2016.
 */
public class RecurringTransactionConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        RecurringTransactionResponse entity = prototypeValidRecurringTransactionResponse().id(1L).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_amount_cents_maps_to_entity_amount_cents() {
        RecurringTransactionResponse entity = prototypeValidRecurringTransactionResponse().amountCents(12439).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getAmountCents(), is(12439));
    }

    @Test
    public void resource_is_active_maps_to_entity_is_active() {
        RecurringTransactionResponse entity = prototypeValidRecurringTransactionResponse().isActive(true).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getIsActive(), is(true));
    }

    @Test
    public void resource_currency_maps_to_entity_currency_external_name() {
        RecurringTransactionResponse entity =
                prototypeValidRecurringTransactionResponse().currency("EUR").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is("EUR"));
    }


    @Test
    public void resource_description_maps_to_entity_description() {
        RecurringTransactionResponse entity =
                prototypeValidRecurringTransactionResponse().description("some description").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getDescription(), is("some description"));
    }

    @Test
    public void resource_recurring_type_maps_to_entity_temporal_expression_type() {
        RecurringTransactionResponse entity =
                prototypeValidRecurringTransactionResponse().temporalExpressionType("Daily").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getRecurringType(), is("Daily"));
    }

    @Test
    public void resource_last_run_one_maps_to_entity_last_run_on() {
        Date known_date = new Date();
        RecurringTransactionResponse entity =
                prototypeValidRecurringTransactionResponse().lastRunOn(known_date).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);
        assertThat(resource.getLastRunOn(), is(known_date));
    }

    @Test
    public void resourceBank_account_is_a_bank_account_resource_with_id_mapping_to_entity_bank_account_id() {
        RecurringTransactionResponse entity =
                prototypeValidRecurringTransactionResponse().bankAccountId(1L).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getBankAccount().getId(), is(1L));
    }

    @Test
    public void resourcePaymentMean_is_a_payment_mean_resource_with_id_mapping_to_entity_payment_mean_id() {
        RecurringTransactionResponse entity =
                prototypeValidRecurringTransactionResponse().paymentMeanId(1L).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getPaymentMean().getId(), is(1L));
    }

    @Test
    public void resource_categories_maps_to_a_list_of_categories_resources_with_id_from_entity_recurringTransaction_ids() {
        RecurringTransactionResponse entity =
                prototypeValidRecurringTransactionResponse().categoryIds(Collections.singletonList(1L)).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getCategories().get(0).getId(), is(1L));
    }

    @Test
    public void resource_categories_is_null_if_entity_categories_is_null() {
        RecurringTransactionResponse entity = prototypeValidRecurringTransactionResponse().categoryIds(null).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionResource resource = converter.convertToResource(entity);

        assertThat(resource.getCategories(), is(nullValue()));
    }


    @Test
    public void entity_amount_cents_maps_to_resource_amount_cents() {
        RecurringTransactionResource resource = RecurringTransactionResource.builder().amountCents(199483).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionRequest entity = converter.buildRequest(resource, null);
        assertThat(entity.getAmountCents(), is(199483));
    }

    @Test
    public void entity_currency_maps_to_resource_currency() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().currency("EUR").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionRequest entity = converter.buildRequest(resource, null);
        assertThat(entity.getCurrency(), is("EUR"));
    }

    @Test
    public void entity_description_maps_to_resource_description() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().description("Known description").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionRequest entity = converter.buildRequest(resource, null);
        assertThat(entity.getDescription(), is("Known description"));
    }

    @Test
    public void entity_is_active_maps_to_resource_is_active() {
        RecurringTransactionResource resource = RecurringTransactionResource.builder().isActive(true).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionRequest entity = converter.buildRequest(resource, null);
        assertThat(entity.isActive(), is(true));
    }

    @Test
    public void entity_recurring_transaction_type_maps_to_resource_recurring_type() {
        RecurringTransactionResource resource = RecurringTransactionResource.builder().recurringType("Monthly").build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionRequest entity = converter.buildRequest(resource, null);
        assertThat(entity.getTemporalExpressionType(), is("Monthly"));
    }

    @Test
    public void entity_bank_account_maps_to_bank_account_entity_with_matching_id() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().bankAccount(BankAccountResource.builder().id(1L).build())
                        .build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionRequest entity = converter.buildRequest(resource, null);
        assertThat(entity.getBankAccountId(), is(1L));
    }

    @Test
    public void entity_payment_mean_maps_to_payment_mean_entity_with_matching_id() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().paymentMean(PaymentMeanResource.builder().id(1L).build())
                        .build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionRequest entity = converter.buildRequest(resource, null);
        assertThat(entity.getPaymentMeanId(), is(1L));
    }

    @Test
    public void entity_categories_maps_to_list_of_category_entity_with_matching_ids() {
        RecurringTransactionResource resource =
                RecurringTransactionResource.builder()
                        .categories(Collections.singletonList(CategoryResource.builder().id(1L).build())).build();

        RecurringTransactionConverter converter = new RecurringTransactionConverter();
        RecurringTransactionRequest entity = converter.buildRequest(resource, null);
        assertThat(entity.getCategoryIds().get(0), is(1L));
    }

    private RecurringTransactionResponse.Builder prototypeValidRecurringTransactionResponse() {
        return RecurringTransactionResponse.builder()
                .categoryIds(new ArrayList<>());
    }
}