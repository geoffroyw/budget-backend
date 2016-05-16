package io.yac.bankaccount.api.converter;

import io.yac.bankaccount.api.BankAccountResource;
import io.yac.bankaccount.api.converter.BankAccountConverter;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.common.domain.SupportedCurrency;
import io.yac.transaction.domain.Transaction;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.transaction.repository.TransactionRepository;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.nullValue;
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
    public void resource_currency_maps_to_entity_currency_external_name() {
        SupportedCurrency knownCurrency = SupportedCurrency.AUD;
        BankAccount entity = prototypeBankAccount().currency(knownCurrency).build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is(SupportedCurrency.AUD.getExternalName()));
    }

    @Test
    public void resource_name_maps_to_entity_name() {
        BankAccount entity = prototypeBankAccount().name("known name").build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getName(), is("known name"));
    }

    private BankAccount.Builder prototypeBankAccount() {
        return BankAccount.builder().currency(SupportedCurrency.EUR)
                .transactions(Collections.singletonList(Transaction.builder().build()));
    }

    @Test
    public void resource_transactions_is_a_list_of_transaction_resources_with_id_mapping_to_entity_transaction_ids() {
        BankAccount entity =
                prototypeBankAccount().transactions(Collections.singletonList(Transaction.builder().id(1L).build()))
                        .build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getTransactions().get(0), is(1L));
    }

    @Test
    public void convert_to_resource_handles_null_transactions_on_entity() {
        BankAccount entity = prototypeBankAccount().transactions(null).build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getTransactions(), is(nullValue()));
    }

    @Test
    public void entity_currency_maps_to_resource_currency() {
        BankAccountResource resource =
                BankAccountResource.builder().currency(SupportedCurrency.ZAR.getExternalName()).build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccount entity = converter.convertToEntity(resource, null);
        assertThat(entity.getCurrency(), is(SupportedCurrency.ZAR));
    }

    @Test
    public void entity_name_maps_to_resource_name() {
        BankAccountResource resource = BankAccountResource.builder().name("Known name").build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccount entity = converter.convertToEntity(resource, null);
        assertThat(entity.getName(), is("Known name"));
    }

    @Test
    public void entity_transactions_maps_to_transaction_entities_with_id_from_resource_transactions() {
        BankAccountResource resource = BankAccountResource.builder().transactions(
                Collections.singletonList(1L)).build();

        Transaction transactionFromRepository = Transaction.builder().id(1L).build();

        TransactionRepository dummyTransactionRepository = mock(TransactionRepository.class);
        when(dummyTransactionRepository.findAll(anyList()))
                .thenReturn(Collections.singletonList(transactionFromRepository));

        BankAccountConverter converter = new BankAccountConverter(dummyTransactionRepository, null);
        BankAccount entity = converter.convertToEntity(resource, null);
        assertThat(entity.getTransactions().get(0), is(transactionFromRepository));
    }

    @Test
    public void convertToEntity_updates_the_entity_if_an_id_is_passed_as_parameter() {
        BankAccountResource resource_of_existing_entity =
                BankAccountResource.builder().currency("CHF").build();
        BankAccount bankAccountFromDb = BankAccount.builder().id(1L).build();

        BankAccountRepository dummyBankAccountRepository = mock(BankAccountRepository.class);
        when(dummyBankAccountRepository.findOne(1L)).thenReturn(bankAccountFromDb);

        BankAccountConverter converter = new BankAccountConverter(null, dummyBankAccountRepository);
        BankAccount entity = converter.convertToEntity(resource_of_existing_entity, 1L);
        assertThat(entity, is(bankAccountFromDb));
    }

}