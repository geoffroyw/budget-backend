package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.budget.api.converter.impl.TransactionConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class BankAccountToTransactionEndpoint implements RelationshipRepository<BankAccountResource, Long, TransactionResource, Long> {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionConverter transactionConverter;

    @Override
    public void setRelation(BankAccountResource source, Long targetId, String fieldName) {

    }

    @Override
    public void setRelations(BankAccountResource source, Iterable<Long> targetIds, String fieldName) {
        BankAccount bankAccount = getBankAccount(source);

        bankAccount.setTransactions((List<Transaction>) transactionRepository.findAll(targetIds));

        bankAccountRepository.save(bankAccount);
    }

    private BankAccount getBankAccount(BankAccountResource source) {
        return getBankAccount(source.getId());
    }

    private BankAccount getBankAccount(Long id) {
        BankAccount bankAccount = bankAccountRepository.findOne(id);
        if (bankAccount == null) {
            throw new ResourceNotFoundException("Bank Account not found " + id);
        }
        return bankAccount;
    }

    @Override
    public void addRelations(BankAccountResource source, Iterable<Long> targetIds, String fieldName) {
        BankAccount bankAccount = getBankAccount(source);
        bankAccount.getTransactions().addAll(
                (Collection<? extends Transaction>) transactionRepository.findAll(targetIds));

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void removeRelations(BankAccountResource source, Iterable<Long> targetIds, String fieldName) {
        BankAccount bankAccount = getBankAccount(source);

        bankAccount.getTransactions().removeAll((List<Transaction>) transactionRepository.findAll(targetIds));

        bankAccountRepository.save(bankAccount);

    }

    @Override
    public TransactionResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }

    @Override
    public Iterable<TransactionResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return getBankAccount(sourceId).getTransactions().stream().map(t -> transactionConverter.convertToResource(t))
                .collect(Collectors.toList());
    }
}