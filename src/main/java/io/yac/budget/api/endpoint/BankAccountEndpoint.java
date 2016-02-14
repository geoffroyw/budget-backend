package io.yac.budget.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
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
    AuthenticationFacade authenticationFacade;

    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private BankAccountConverter converter;

    public BankAccountEndpoint() {
    }

    @VisibleForTesting
    public BankAccountEndpoint(BankAccountRepository bankAccountRepository, AuthenticationFacade authenticationFacade,
                               BankAccountConverter bankAccountConverter) {
        this.repository = bankAccountRepository;
        this.authenticationFacade = authenticationFacade;
        this.converter = bankAccountConverter;
    }

    @Override
    public BankAccountResource findOne(Long id, QueryParams queryParams) {
        BankAccount bankAccount = repository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (bankAccount == null) {
            throw new ResourceNotFoundException("Bank Account not found");
        }


        return converter.convertToResource(bankAccount);
    }

    @Override
    public Iterable<BankAccountResource> findAll(QueryParams queryParams) {
        return StreamSupport.stream(repository.findByOwner(authenticationFacade.getCurrentUser()).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<BankAccountResource> findAll(Iterable<Long> ids, QueryParams queryParams) {
        return StreamSupport
                .stream(repository.findByOwnerAndIds(authenticationFacade.getCurrentUser(), ids).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        BankAccount bankAccount = repository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (bankAccount == null) {
            throw new ResourceNotFoundException("Bank Account not found");
        }
        repository.delete(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BankAccountResource save(BankAccountResource resource) {
        BankAccount entity = converter.convertToEntity(resource);
        entity.setOwner(authenticationFacade.getCurrentUser());
        return converter.convertToResource(repository.save(entity));
    }
}
