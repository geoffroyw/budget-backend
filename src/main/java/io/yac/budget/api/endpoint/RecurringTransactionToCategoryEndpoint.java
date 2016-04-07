package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.CategoryConverter;
import io.yac.budget.api.converter.impl.RecurringTransactionConverter;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.Category;
import io.yac.budget.recurring.transactions.client.RecurringTransactionRequest;
import io.yac.budget.recurring.transactions.client.resources.RecurringTransactionResponse;
import io.yac.budget.repository.CategoryRepository;
import io.yac.services.clients.recurringtransaction.RecurringTransactionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class RecurringTransactionToCategoryEndpoint implements RelationshipRepository<RecurringTransactionResource, Long, CategoryResource, Long> {

    @Autowired
    RecurringTransactionClient recurringTransactionClient;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryConverter categoryConverter;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    RecurringTransactionConverter recurringTransactionConverter;

    @Override
    public void setRelation(RecurringTransactionResource source, Long targetId, String fieldName) {

    }

    @Override
    public void setRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {
        RecurringTransactionResponse recurringTransactionResponse = getRecurringTransaction(source);

        RecurringTransactionRequest request = recurringTransactionConverter.buildRequest(recurringTransactionResponse);
        request.setCategoryIds(StreamSupport.stream(targetIds.spliterator(), false).collect(Collectors.toList()));

        recurringTransactionClient.update(source.getId(), request);
    }

    private RecurringTransactionResponse getRecurringTransaction(RecurringTransactionResource source) {
        return getRecurringTransaction(source.getId());
    }

    private RecurringTransactionResponse getRecurringTransaction(Long id) {
        try {
            RecurringTransactionResponse recurringTransactionResponse = recurringTransactionClient.getById(id);
            if (recurringTransactionResponse == null ||
                    !recurringTransactionResponse.getOwnerId().equals(authenticationFacade.getCurrentUser().getId())) {
                throw new ResourceNotFoundException("RecurringTransaction not found " + id);
            }
            return recurringTransactionResponse;
        } catch (io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException e) {
            throw new ResourceNotFoundException("RecurringTransaction not found " + id);
        }
    }

    @Override
    public void addRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {
        RecurringTransactionResponse recurringTransactionResponse = getRecurringTransaction(source);
        RecurringTransactionRequest request = recurringTransactionConverter.buildRequest(recurringTransactionResponse);
        request.getCategoryIds()
                .addAll((StreamSupport.stream(targetIds.spliterator(), false).collect(Collectors.toList())));


        recurringTransactionClient.update(source.getId(), request);
    }

    @Override
    public void removeRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {
        RecurringTransactionResponse recurringTransactionResponse = getRecurringTransaction(source);
        RecurringTransactionRequest request = recurringTransactionConverter.buildRequest(recurringTransactionResponse);
        request.getCategoryIds()
                .removeAll(StreamSupport.stream(targetIds.spliterator(), false).collect(Collectors.toList()));
        recurringTransactionClient.update(source.getId(), request);
    }

    @Override
    public CategoryResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }

    @Override
    public Iterable<CategoryResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        List<Long> categoryIds = getRecurringTransaction(sourceId).getCategoryIds();
        Iterable<Category> categories = categoryRepository.findAll(categoryIds);
        return StreamSupport.stream(categories.spliterator(), false)
                .map(category -> categoryConverter.convertToResource(category)).collect(
                        Collectors.toSet());
    }
}
