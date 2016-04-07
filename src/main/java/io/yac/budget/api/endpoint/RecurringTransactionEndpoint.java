package io.yac.budget.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.RecurringTransactionConverter;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.recurring.transactions.client.RecurringTransactionRequest;
import io.yac.budget.recurring.transactions.client.resources.RecurringTransactionResponse;
import io.yac.services.clients.recurringtransaction.RecurringTransactionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 19/02/2016.
 */
@Component
public class RecurringTransactionEndpoint implements ResourceRepository<RecurringTransactionResource, Long> {

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    RecurringTransactionClient recurringTransactionClient;

    @Autowired
    RecurringTransactionConverter converter;

    public RecurringTransactionEndpoint() {
    }

    @VisibleForTesting
    public RecurringTransactionEndpoint(RecurringTransactionClient recurringTransactionClient,
                                        AuthenticationFacade authenticationFacade,
                                        RecurringTransactionConverter converter) {

        this.recurringTransactionClient = recurringTransactionClient;
        this.authenticationFacade = authenticationFacade;
        this.converter = converter;
    }

    @Override
    public RecurringTransactionResource findOne(Long id, QueryParams queryParams) {

        try {
            RecurringTransactionResponse transaction = recurringTransactionClient.getById(id);

            if (transaction == null ||
                    !transaction.getOwnerId().equals(authenticationFacade.getCurrentUser().getId())) {
                throw new ResourceNotFoundException("Recurring Transaction not found");
            }

            return converter.convertToResource(transaction);

        } catch (io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Recurring Transaction not found");
        }

    }

    @Override
    public Iterable<RecurringTransactionResource> findAll(QueryParams queryParams) {
        return recurringTransactionClient.findAll().stream()
                .filter((item) -> authenticationFacade.getCurrentUser().getId().equals(item.getOwnerId()))
                .map(a -> converter.convertToResource(a)).collect(Collectors.toList());
    }

    @Override
    public Iterable<RecurringTransactionResource> findAll(Iterable<Long> ids, QueryParams queryParams) {
        List<Long> idList = new ArrayList<>();
        for (Long id : ids) {
            idList.add(id);
        }

        return recurringTransactionClient.findAll().stream()
                .filter((item) -> idList.contains(item.getId()) &&
                        authenticationFacade.getCurrentUser().getId().equals(item.getOwnerId()))
                .map(a -> converter.convertToResource(a)).collect(Collectors.toList());
    }


    @Override
    public void delete(Long id) {
        try {
            RecurringTransactionResponse recurringTransaction = recurringTransactionClient.getById(id);

            if (recurringTransaction == null ||
                    !recurringTransaction.getOwnerId().equals(authenticationFacade.getCurrentUser().getId())) {
                throw new ResourceNotFoundException("Recurring Transaction not found");
            }

            recurringTransactionClient.delete(id);


        } catch (io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Transaction not found");
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public RecurringTransactionResource save(RecurringTransactionResource resource) {
        RecurringTransactionRequest entity = converter.buildRequest(resource, authenticationFacade.getCurrentUser().getId());
        return converter.convertToResource(recurringTransactionClient.create(entity));
    }
}
