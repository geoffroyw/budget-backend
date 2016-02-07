package io.yac.budget.api.converter.impl;

import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.domain.Category;
import org.springframework.stereotype.Service;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class CategoryConverter implements ResourceEntityConverter<CategoryResource, Category> {
    @Override
    public CategoryResource convertToResource(Category entity) {
        return CategoryResource.builder().id(entity.getId()).name(entity.getName()).build();
    }

    @Override
    public Category convertToEntity(CategoryResource resource) {
        return Category.builder().name(resource.getName()).id(resource.getId()).build();
    }
}
