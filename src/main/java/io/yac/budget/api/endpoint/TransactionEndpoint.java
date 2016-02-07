package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
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
    private TransactionRepository repository;

    @Autowired
    private TransactionConverter converter;

    @Override
    public TransactionResource findOne(Long id, QueryParams queryParams) {
        Transaction Transaction = repository.findOne(id);
        if (Transaction == null) {
            throw new ResourceNotFoundException("Bank Account not found");
        }

        return converter.convertToResource(Transaction);
    }

    @Override
    public Iterable<TransactionResource> findAll(QueryParams queryParams) {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<TransactionResource> findAll(Iterable<Long> longs, QueryParams queryParams) {
        return StreamSupport.stream(repository.findAll(longs).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public TransactionResource save(TransactionResource resource) {
        Transaction entity = converter.convertToEntity(resource);
        return converter.convertToResource(repository.save(entity));
    }
}
