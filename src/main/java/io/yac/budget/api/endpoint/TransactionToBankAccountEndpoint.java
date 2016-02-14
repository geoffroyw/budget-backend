package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.BankAccountConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class TransactionToBankAccountEndpoint implements RelationshipRepository<TransactionResource, Long, BankAccountResource, Long> {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    BankAccountConverter bankAccountConverter;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Override
    public void setRelation(TransactionResource source, Long targetId, String fieldName) {
        Transaction transaction =
                transactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), source.getId());
        BankAccount target = bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), targetId);

        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found " + source.getId());
        }

        if (target == null) {
            throw new ResourceNotFoundException("Bank Account not found" + targetId);
        }

        transaction.setBankAccount(target);

        transactionRepository.save(transaction);
    }

    @Override
    public void setRelations(TransactionResource source, Iterable<Long> targetIds, String fieldName) {

    }

    @Override
    public void addRelations(TransactionResource source, Iterable<Long> targetIds, String fieldName) {

    }

    @Override
    public void removeRelations(TransactionResource source, Iterable<Long> targetIds, String fieldName) {

    }

    @Override
    public BankAccountResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        Transaction transaction =
                transactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), sourceId);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found " + sourceId);
        }
        return bankAccountConverter.convertToResource(transaction.getBankAccount());
    }

    @Override
    public Iterable<BankAccountResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }
}
