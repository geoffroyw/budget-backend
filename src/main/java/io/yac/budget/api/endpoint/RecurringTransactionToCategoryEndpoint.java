package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.CategoryConverter;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.Category;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.repository.CategoryRepository;
import io.yac.budget.repository.RecurringTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class RecurringTransactionToCategoryEndpoint implements RelationshipRepository<RecurringTransactionResource, Long, CategoryResource, Long> {

    @Autowired
    RecurringTransactionRepository RecurringTransactionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryConverter categoryConverter;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Override
    public void setRelation(RecurringTransactionResource source, Long targetId, String fieldName) {

    }

    @Override
    public void setRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {
        RecurringTransaction RecurringTransaction = getRecurringTransaction(source);

        RecurringTransaction.setCategories((List<Category>) categoryRepository.findAll(targetIds));
        RecurringTransactionRepository.save(RecurringTransaction);
    }

    private RecurringTransaction getRecurringTransaction(RecurringTransactionResource source) {
        return getRecurringTransaction(source.getId());
    }

    private RecurringTransaction getRecurringTransaction(Long id) {
        RecurringTransaction RecurringTransaction =
                RecurringTransactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (RecurringTransaction == null) {
            throw new ResourceNotFoundException("RecurringTransaction not found " + id);
        }
        return RecurringTransaction;
    }

    @Override
    public void addRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {
        RecurringTransaction RecurringTransaction = getRecurringTransaction(source);
        RecurringTransaction.getCategories().addAll((Collection<Category>) categoryRepository.findAll(targetIds));
        RecurringTransactionRepository.save(RecurringTransaction);
    }

    @Override
    public void removeRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {
        RecurringTransaction RecurringTransaction = getRecurringTransaction(source);
        RecurringTransaction.getCategories().removeAll((Collection<Category>) categoryRepository.findAll(targetIds));
        RecurringTransactionRepository.save(RecurringTransaction);
    }

    @Override
    public CategoryResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }

    @Override
    public Iterable<CategoryResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return getRecurringTransaction(sourceId).getCategories().stream()
                .map(category -> categoryConverter.convertToResource(category)).collect(
                        Collectors.toSet());
    }
}
