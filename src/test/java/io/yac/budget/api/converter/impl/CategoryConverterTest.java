package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.Category;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.CategoryRepository;
import io.yac.budget.repository.TransactionRepository;
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
public class CategoryConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        Category entity = prototypeCategory().id(1L).build();

        CategoryConverter converter = new CategoryConverter();
        CategoryResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_name_maps_to_entity_name() {
        Category entity = prototypeCategory().name("known name").build();

        CategoryConverter converter = new CategoryConverter();
        CategoryResource resource = converter.convertToResource(entity);
        assertThat(resource.getName(), is("known name"));
    }


    @Test
    public void resource_transactions_is_a_list_of_transaction_resources_with_ids_coming_from_entity_transaction_ids() {
        Category entity =
                prototypeCategory().transactions(Collections.singletonList(Transaction.builder().id(1L).build()))
                        .build();

        CategoryConverter converter = new CategoryConverter();
        CategoryResource resource = converter.convertToResource(entity);
        assertThat(resource.getTransactions().get(0).getId(), is(1L));
    }

    @Test
    public void resource_transactions_is_null_if_entity_transactions_is_null() {
        Category entity = prototypeCategory().transactions(null).build();

        CategoryConverter converter = new CategoryConverter();
        CategoryResource resource = converter.convertToResource(entity);
        assertThat(resource.getTransactions(), is(nullValue()));
    }

    private Category.Builder prototypeCategory() {
        return Category.builder().transactions(Collections.singletonList(Transaction.builder().build()));
    }

    @Test
    public void entity_name_maps_to_resource_name() {
        CategoryResource resource = CategoryResource.builder().name("Known name").build();

        CategoryConverter converter = new CategoryConverter();
        Category entity = converter.convertToEntity(resource);
        assertThat(entity.getName(), is("Known name"));
    }

    @Test
    public void entity_transactions_maps_to_transaction_entities_with_id_from_resource_transactions() {
        CategoryResource resource = CategoryResource.builder().transactions(
                Collections.singletonList(TransactionResource.builder().id(1L).build())).build();

        Transaction transactionFromRepository = Transaction.builder().id(1L).build();

        TransactionRepository dummyTransactionRepository = mock(TransactionRepository.class);
        when(dummyTransactionRepository.findAll(anyList()))
                .thenReturn(Collections.singletonList(transactionFromRepository));

        CategoryConverter converter = new CategoryConverter(dummyTransactionRepository, null);
        Category entity = converter.convertToEntity(resource);
        assertThat(entity.getTransactions().get(0), is(transactionFromRepository));
    }

    @Test
    public void convertToEntity_updates_the_entity_if_the_resource_is_passed_with_an_id() {
        CategoryResource resource_of_existing_entity =
                CategoryResource.builder().id(1L).name("Some name").build();
        Category CategoryFromDb = Category.builder().id(1L).build();

        CategoryRepository dummyCategoryRepository = mock(CategoryRepository.class);
        when(dummyCategoryRepository.findOne(1L)).thenReturn(CategoryFromDb);

        CategoryConverter converter = new CategoryConverter(null, dummyCategoryRepository);
        Category entity = converter.convertToEntity(resource_of_existing_entity);
        assertThat(entity, is(CategoryFromDb));
    }
}