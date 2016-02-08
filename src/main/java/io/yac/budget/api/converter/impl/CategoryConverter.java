package io.yac.budget.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.Category;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class CategoryConverter implements ResourceEntityConverter<CategoryResource, Category> {

    @Autowired
    TransactionRepository transactionRepository;

    public CategoryConverter() {
    }

    @VisibleForTesting
    CategoryConverter(TransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    @Override
    public CategoryResource convertToResource(Category entity) {
        return CategoryResource.builder().id(entity.getId()).name(entity.getName()).transactions(
                entity.getTransactions().stream().map(t -> TransactionResource.builder().id(t.getId()).build())
                        .collect(Collectors.toList())).build();
    }

    @Override
    public Category convertToEntity(CategoryResource resource) {
        return Category.builder().name(resource.getName()).id(resource.getId()).transactions(
                resource.getTransactions() == null ? null : (List<Transaction>) transactionRepository
                        .findAll(resource.getTransactions().stream().map(
                                TransactionResource::getId).collect(Collectors.toSet()))).build();
    }
}
