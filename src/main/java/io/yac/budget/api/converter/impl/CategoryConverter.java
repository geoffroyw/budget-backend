package io.yac.budget.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.domain.Category;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.CategoryRepository;
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

    @Autowired
    CategoryRepository categoryRepository;

    public CategoryConverter() {
    }

    @VisibleForTesting
    CategoryConverter(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResource convertToResource(Category entity) {
        return CategoryResource.builder().id(entity.getId()).name(entity.getName())
                .transactions(entity.getTransactions() == null ? null :
                              entity.getTransactions().stream()
                                      .map(Transaction::getId)
                                      .collect(Collectors.toList())).build();
    }

    @Override
    public Category convertToEntity(CategoryResource resource) {
        Category category;
        if (resource.getId() == null) {
            category = new Category();
        } else {
            category = categoryRepository.findOne(resource.getId());
        }


        category.setName(resource.getName());
        category.setTransactions(resource.getTransactions() == null ? null : (List<Transaction>) transactionRepository
                .findAll(resource.getTransactions()));

        return category;
    }
}
