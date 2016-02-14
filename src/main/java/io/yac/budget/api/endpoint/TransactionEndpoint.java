package io.yac.budget.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.TransactionConverter;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Component
public class TransactionEndpoint implements ResourceRepository<TransactionResource, Long> {

    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private TransactionConverter converter;

    public TransactionEndpoint() {
    }

    @VisibleForTesting
    public TransactionEndpoint(TransactionRepository transactionRepository,
                               AuthenticationFacade authenticationFacade,
                               TransactionConverter transactionConverter) {

        this.repository = transactionRepository;
        this.authenticationFacade = authenticationFacade;
        this.converter = transactionConverter;
    }

    @Override
    public TransactionResource findOne(Long id, QueryParams queryParams) {
        Transaction transaction = repository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        return converter.convertToResource(transaction);
    }

    @Override
    public Iterable<TransactionResource> findAll(QueryParams queryParams) {
        return StreamSupport
                .stream(repository.findAllByOwner(authenticationFacade.getCurrentUser()).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<TransactionResource> findAll(Iterable<Long> ids, QueryParams queryParams) {
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
    public TransactionResource save(TransactionResource resource) {
        Transaction entity = converter.convertToEntity(resource);
        entity.setOwner(authenticationFacade.getCurrentUser());
        return converter.convertToResource(repository.save(entity));
    }
}
