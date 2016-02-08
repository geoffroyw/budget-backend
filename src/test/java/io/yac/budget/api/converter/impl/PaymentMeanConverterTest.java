package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.TransactionRepository;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class PaymentMeanConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        PaymentMean entity = prototypePaymentMean().id(1L).build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMeanResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_name_maps_to_entity_name() {
        PaymentMean entity = prototypePaymentMean().name("known name").build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMeanResource resource = converter.convertToResource(entity);
        assertThat(resource.getName(), is("known name"));
    }

    @Test
    public void resource_currency_maps_to_entity_currency() {
        PaymentMean entity = prototypePaymentMean().currency("known currency").build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMeanResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is("known currency"));
    }

    @Test
    public void resource_transactions_maps_to_a_list_of_transaction_resource_with_matching_ids() {
        PaymentMean entity = prototypePaymentMean()
                .transactions(Arrays.asList(Transaction.builder().id(1L).build(), Transaction.builder().id(2L).build()))
                .build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMeanResource resource = converter.convertToResource(entity);
        assertThat(resource.getTransactions(), hasSize(2));
        assertThat(resource.getTransactions().get(0).getId(), is(1L));
        assertThat(resource.getTransactions().get(1).getId(), is(2L));
    }

    private PaymentMean.Builder prototypePaymentMean() {
        return PaymentMean.builder().transactions(Collections.singletonList(Transaction.builder().build()));
    }

    @Test
    public void entity_id_maps_to_resource_id() {
        PaymentMeanResource resource = PaymentMeanResource.builder().id(1L).build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMean entity = converter.convertToEntity(resource);
        assertThat(entity.getId(), is(1L));
    }

    @Test
    public void entity_name_maps_to_resource_name() {
        PaymentMeanResource resource = PaymentMeanResource.builder().name("Known name").build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMean entity = converter.convertToEntity(resource);
        assertThat(entity.getName(), is("Known name"));
    }

    @Test
    public void entity_currency_maps_to_resource_currency() {
        PaymentMeanResource resource = PaymentMeanResource.builder().currency("Known currency").build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMean entity = converter.convertToEntity(resource);
        assertThat(entity.getCurrency(), is("Known currency"));
    }

    @Test
    public void entity_transactions_maps_to_transaction_entities_with_id_from_resource_transactions() {
        PaymentMeanResource resource = PaymentMeanResource.builder().transactions(
                Collections.singletonList(TransactionResource.builder().id(1L).build())).build();

        Transaction transactionFromRepository = Transaction.builder().id(1L).build();

        TransactionRepository dummyTransactionRepository = mock(TransactionRepository.class);
        when(dummyTransactionRepository.findAll(anyList()))
                .thenReturn(Collections.singletonList(transactionFromRepository));

        PaymentMeanConverter converter = new PaymentMeanConverter(dummyTransactionRepository);
        PaymentMean entity = converter.convertToEntity(resource);
        assertThat(entity.getTransactions().get(0), is(transactionFromRepository));
    }


}