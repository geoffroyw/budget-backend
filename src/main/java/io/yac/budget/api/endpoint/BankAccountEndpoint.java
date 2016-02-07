package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.budget.api.converter.impl.BankAccountConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Component
public class BankAccountEndpoint implements ResourceRepository<BankAccountResource, Long> {

    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private BankAccountConverter converter;

    @Override
    public BankAccountResource findOne(Long id, QueryParams queryParams) {
        BankAccount bankAccount = repository.findOne(id);
        if (bankAccount == null) {
            throw new ResourceNotFoundException("Bank Account not found");
        }

        return converter.convertToResource(bankAccount);
    }

    @Override
    public Iterable<BankAccountResource> findAll(QueryParams queryParams) {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<BankAccountResource> findAll(Iterable<Long> longs, QueryParams queryParams) {
        return StreamSupport.stream(repository.findAll(longs).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public BankAccountResource save(BankAccountResource resource) {
        BankAccount entity = converter.convertToEntity(resource);
        return converter.convertToResource(repository.save(entity));
    }
}
