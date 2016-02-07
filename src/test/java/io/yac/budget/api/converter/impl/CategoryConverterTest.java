package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.domain.Category;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class CategoryConverterTest {

    @Test
    public void resource_id_maps_to_entity_id() {
        Category entity = Category.builder().id(1L).build();

        CategoryConverter converter = new CategoryConverter();
        CategoryResource resource = converter.convertToResource(entity);
        assertThat(resource.getId(), is(1L));
    }

    @Test
    public void resource_name_maps_to_entity_name() {
        Category entity = Category.builder().name("known name").build();

        CategoryConverter converter = new CategoryConverter();
        CategoryResource resource = converter.convertToResource(entity);
        assertThat(resource.getName(), is("known name"));
    }


    @Test
    public void entity_id_maps_to_resource_id() {
        CategoryResource resource = CategoryResource.builder().id(1L).build();

        CategoryConverter converter = new CategoryConverter();
        Category entity = converter.convertToEntity(resource);
        assertThat(entity.getId(), is(1L));
    }


    @Test
    public void entity_name_maps_to_resource_name() {
        CategoryResource resource = CategoryResource.builder().name("Known name").build();

        CategoryConverter converter = new CategoryConverter();
        Category entity = converter.convertToEntity(resource);
        assertThat(entity.getName(), is("Known name"));
    }

}