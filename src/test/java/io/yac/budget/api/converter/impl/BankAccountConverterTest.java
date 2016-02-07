package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.domain.BankAccount;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class BankAccountConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        BankAccount entity = BankAccount.builder().id(1L).build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_currency_maps_to_entity_currency() {
        BankAccount entity = BankAccount.builder().currency("known currency").build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is("known currency"));
    }

    @Test
    public void resource_name_maps_to_entity_name() {
        BankAccount entity = BankAccount.builder().name("known name").build();

        BankAccountConverter converter = new BankAccountConverter();
        BankAccountResource resource = converter.convertToResource(entity);
        assertThat(resource.getName(), is("known name"));
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

}