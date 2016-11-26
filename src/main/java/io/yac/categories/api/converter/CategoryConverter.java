package io.yac.categories.api.converter;

import io.yac.categories.api.CategoryResource;
import io.yac.categories.domain.Category;
import io.yac.categories.repository.CategoryRepository;
import io.yac.common.api.converter.ResourceEntityConverter;
import io.yac.transaction.domain.Transaction;
import io.yac.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class CategoryConverter implements ResourceEntityConverter<CategoryResource, Category> {

    private final TransactionRepository transactionRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryConverter(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
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
    public Category convertToEntity(CategoryResource resource, Long id) {
        Category category;
        if (id == null) {
            category = new Category();
        } else {
            category = categoryRepository.findOne(id);
        }


        category.setName(resource.getName());
        category.setTransactions(resource.getTransactions() == null ? null : (List<Transaction>) transactionRepository
                .findAll(resource.getTransactions()));

        return category;
    }
}
