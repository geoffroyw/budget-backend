package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.TransactionRepository;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class BankAccountConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        BankAccount entity = prototypeBankAccount().id(1L).build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_currency_maps_to_entity_currency() {
        BankAccount entity = prototypeBankAccount().currency("known currency").build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is("known currency"));
    }

    @Test
    public void resource_name_maps_to_entity_name() {
        BankAccount entity = prototypeBankAccount().name("known name").build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getName(), is("known name"));
    }

    private BankAccount.Builder prototypeBankAccount() {
        return BankAccount.builder().transactions(Collections.singletonList(Transaction.builder().build()));
    }

    @Test
    public void resource_transactions_is_a_list_of_transaction_resources_with_id_mapping_to_entity_transaction_ids() {
        BankAccount entity =
                prototypeBankAccount().transactions(Collections.singletonList(Transaction.builder().id(1L).build()))
                        .build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getTransactions().get(0).getId(), is(1L));
    }


    @Test
    public void entity_id_maps_to_resource_id() {
        BankAccountResource resource = BankAccountResource.builder().id(1L).build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccount entity = converter.convertToEntity(resource);
        assertThat(entity.getId(), is(1L));
    }

    @Test
    public void entity_currency_maps_to_resource_currency() {
        BankAccountResource resource = BankAccountResource.builder().currency("Known currency").build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccount entity = converter.convertToEntity(resource);
        assertThat(entity.getCurrency(), is("Known currency"));
    }

    @Test
    public void entity_name_maps_to_resource_name() {
        BankAccountResource resource = BankAccountResource.builder().name("Known name").build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccount entity = converter.convertToEntity(resource);
        assertThat(entity.getName(), is("Known name"));
    }

    @Test
    public void entity_transactions_maps_to_transaction_entities_with_id_from_resource_transactions() {
        BankAccountResource resource = BankAccountResource.builder().transactions(
                Collections.singletonList(TransactionResource.builder().id(1L).build())).build();

        Transaction transactionFromRepository = Transaction.builder().id(1L).build();

        TransactionRepository dummyTransactionRepository = mock(TransactionRepository.class);
        when(dummyTransactionRepository.findAll(anyList()))
                .thenReturn(Collections.singletonList(transactionFromRepository));

        BankAccountConverter converter = new BankAccountConverter(dummyTransactionRepository);
        BankAccount entity = converter.convertToEntity(resource);
        assertThat(entity.getTransactions().get(0), is(transactionFromRepository));
    }

}