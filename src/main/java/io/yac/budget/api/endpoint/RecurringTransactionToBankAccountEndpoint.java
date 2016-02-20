package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.BankAccountConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.RecurringTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class RecurringTransactionToBankAccountEndpoint implements RelationshipRepository<RecurringTransactionResource, Long, BankAccountResource, Long> {

    @Autowired
    RecurringTransactionRepository RecurringTransactionRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    BankAccountConverter bankAccountConverter;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Override
    public void setRelation(RecurringTransactionResource source, Long targetId, String fieldName) {
        RecurringTransaction RecurringTransaction =
                RecurringTransactionRepository
                        .findOneByOwnerAndId(authenticationFacade.getCurrentUser(), source.getId());
        BankAccount target = bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), targetId);

        if (RecurringTransaction == null) {
            throw new ResourceNotFoundException("RecurringTransaction not found " + source.getId());
        }

        if (target == null) {
            throw new ResourceNotFoundException("Bank Account not found" + targetId);
        }

        RecurringTransaction.setBankAccount(target);

        RecurringTransactionRepository.save(RecurringTransaction);
    }

    @Override
    public void setRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {

    }

    @Override
    public void addRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {

    }

    @Override
    public void removeRelations(RecurringTransactionResource source, Iterable<Long> targetIds, String fieldName) {

    }

    @Override
    public BankAccountResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        RecurringTransaction RecurringTransaction =
                RecurringTransactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), sourceId);
        if (RecurringTransaction == null) {
            throw new ResourceNotFoundException("RecurringTransaction not found " + sourceId);
        }
        return bankAccountConverter.convertToResource(RecurringTransaction.getBankAccount());
    }

    @Override
    public Iterable<BankAccountResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }
}
