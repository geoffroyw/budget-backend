package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.budget.api.converter.impl.CategoryConverter;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.domain.Category;
import io.yac.budget.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Component
public class CategoryEndpoint implements ResourceRepository<CategoryResource, Long> {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryConverter converter;

    @Override
    public CategoryResource findOne(Long id, QueryParams queryParams) {
        Category Category = repository.findOne(id);
        if (Category == null) {
            throw new ResourceNotFoundException("Bank Account not found");
        }

        return converter.convertToResource(Category);
    }

    @Override
    public Iterable<CategoryResource> findAll(QueryParams queryParams) {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<CategoryResource> findAll(Iterable<Long> longs, QueryParams queryParams) {
        return StreamSupport.stream(repository.findAll(longs).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CategoryResource save(CategoryResource resource) {
        Category entity = converter.convertToEntity(resource);
        return converter.convertToResource(repository.save(entity));
    }
}
