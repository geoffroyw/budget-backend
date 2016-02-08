package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.budget.api.converter.impl.TransactionConverter;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.Category;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.CategoryRepository;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class CategoryToTransactionEndpoit implements RelationshipRepository<CategoryResource, Long, TransactionResource, Long> {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionConverter transactionConverter;

    @Override
    public void setRelation(CategoryResource source, Long targetId, String fieldName) {

    }

    @Override
    public void setRelations(CategoryResource source, Iterable<Long> targetIds, String fieldName) {
        Category category = getCategory(source);

        category.setTransactions((List<Transaction>) transactionRepository.findAll(targetIds));

        categoryRepository.save(category);

    }

    private Category getCategory(CategoryResource source) {
        return getCategory(source.getId());
    }

    private Category getCategory(Long id) {
        Category category = categoryRepository.findOne(id);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found for id " + id);
        }
        return category;
    }

    @Override
    public void addRelations(CategoryResource source, Iterable<Long> targetIds, String fieldName) {
        Category category = getCategory(source);

        category.getTransactions().addAll((Collection<? extends Transaction>) transactionRepository.findAll(targetIds));

        categoryRepository.save(category);
    }

    @Override
    public void removeRelations(CategoryResource source, Iterable<Long> targetIds, String fieldName) {
        Category category = getCategory(source);
        category.getTransactions().removeAll((Collection<Transaction>) transactionRepository.findAll(targetIds));
        categoryRepository.save(category);
    }

    @Override
    public TransactionResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }

    @Override
    public Iterable<TransactionResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return getCategory(sourceId).getTransactions().stream()
                .map(transaction -> transactionConverter.convertToResource(transaction)).collect(Collectors.toSet());
    }
}
