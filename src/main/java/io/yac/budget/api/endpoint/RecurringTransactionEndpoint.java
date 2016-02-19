package io.yac.budget.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.RecurringTransactionConverter;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.repository.RecurringTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 19/02/2016.
 */
@Component
public class RecurringTransactionEndpoint implements ResourceRepository<RecurringTransactionResource, Long> {

    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    private RecurringTransactionRepository repository;
    @Autowired
    private RecurringTransactionConverter converter;

    public RecurringTransactionEndpoint() {
    }

    @VisibleForTesting
    public RecurringTransactionEndpoint(RecurringTransactionRepository repository,
                                        AuthenticationFacade authenticationFacade,
                                        RecurringTransactionConverter converter) {

        this.repository = repository;
        this.authenticationFacade = authenticationFacade;
        this.converter = converter;
    }

    @Override
    public RecurringTransactionResource findOne(Long id, QueryParams queryParams) {
        RecurringTransaction transaction = repository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (transaction == null) {
            throw new ResourceNotFoundException("Recurring Transaction not found");
        }

        return converter.convertToResource(transaction);
    }

    @Override
    public Iterable<RecurringTransactionResource> findAll(QueryParams queryParams) {
        return StreamSupport
                .stream(repository.findAllByOwner(authenticationFacade.getCurrentUser()).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<RecurringTransactionResource> findAll(Iterable<Long> ids, QueryParams queryParams) {
        return StreamSupport
                .stream(repository.findAllByOwnerAndIds(authenticationFacade.getCurrentUser(), ids).spliterator(),
                        false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (repository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id) == null) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        repository.delete(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecurringTransactionResource save(RecurringTransactionResource resource) {
        RecurringTransaction entity = converter.convertToEntity(resource);
        entity.setOwner(authenticationFacade.getCurrentUser());
        return converter.convertToResource(repository.save(entity));
    }
}
