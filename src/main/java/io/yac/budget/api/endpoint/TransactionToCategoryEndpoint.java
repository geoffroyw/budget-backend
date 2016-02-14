package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.CategoryConverter;
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
public class TransactionToCategoryEndpoint implements RelationshipRepository<TransactionResource, Long, CategoryResource, Long> {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryConverter categoryConverter;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Override
    public void setRelation(TransactionResource source, Long targetId, String fieldName) {

    }

    @Override
    public void setRelations(TransactionResource source, Iterable<Long> targetIds, String fieldName) {
        Transaction transaction = getTransaction(source);

        transaction.setCategories((List<Category>) categoryRepository.findAll(targetIds));
        transactionRepository.save(transaction);
    }

    private Transaction getTransaction(TransactionResource source) {
        return getTransaction(source.getId());
    }

    private Transaction getTransaction(Long id) {
        Transaction transaction = transactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found " + id);
        }
        return transaction;
    }

    @Override
    public void addRelations(TransactionResource source, Iterable<Long> targetIds, String fieldName) {
        Transaction transaction = getTransaction(source);
        transaction.getCategories().addAll((Collection<Category>) categoryRepository.findAll(targetIds));
        transactionRepository.save(transaction);
    }

    @Override
    public void removeRelations(TransactionResource source, Iterable<Long> targetIds, String fieldName) {
        Transaction transaction = getTransaction(source);
        transaction.getCategories().removeAll((Collection<Category>) categoryRepository.findAll(targetIds));
        transactionRepository.save(transaction);
    }

    @Override
    public CategoryResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }

    @Override
    public Iterable<CategoryResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return getTransaction(sourceId).getCategories().stream()
                .map(category -> categoryConverter.convertToResource(category)).collect(
                        Collectors.toSet());
    }
}
