package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.domain.PaymentMean;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class PaymentMeanConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        PaymentMean entity = PaymentMean.builder().id(1L).build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMeanResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_name_maps_to_entity_name() {
        PaymentMean entity = PaymentMean.builder().name("known name").build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMeanResource resource = converter.convertToResource(entity);
        assertThat(resource.getName(), is("known name"));
    }

    @Test
    public void resource_currency_maps_to_entity_currency() {
        PaymentMean entity = PaymentMean.builder().currency("known currency").build();

        PaymentMeanConverter converter = new PaymentMeanConverter();
        PaymentMeanResource resource = converter.convertToResource(entity);
        assertThat(resource.getCurrency(), is("known currency"));
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
}