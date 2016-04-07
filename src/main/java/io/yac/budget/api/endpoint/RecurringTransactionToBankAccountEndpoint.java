package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.BankAccountConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.recurring.transactions.client.RecurringTransactionRequest;
import io.yac.budget.recurring.transactions.client.resources.RecurringTransactionResponse;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.services.clients.recurringtransaction.RecurringTransactionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class RecurringTransactionToBankAccountEndpoint implements RelationshipRepository<RecurringTransactionResource, Long, BankAccountResource, Long> {

    @Autowired
    RecurringTransactionClient recurringTransactionClient;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    BankAccountConverter bankAccountConverter;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Override
    public void setRelation(RecurringTransactionResource source, Long targetId, String fieldName) {

        try {
            RecurringTransactionResponse recurringTransaction = recurringTransactionClient
                    .getById(source.getId());

            if (recurringTransaction == null) {
                throw new ResourceNotFoundException("RecurringTransaction not found " + source.getId());
            }

            if (!recurringTransaction.getOwnerId().equals(authenticationFacade.getCurrentUser().getId())) {
                throw new ResourceNotFoundException("RecurringTransaction not found " + source.getId());
            }

            BankAccount target =
                    bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), targetId);


            if (target == null) {
                throw new ResourceNotFoundException("Bank Account not found" + targetId);
            }

            RecurringTransactionRequest request =
                    RecurringTransactionRequest.builder().bankAccountId(recurringTransaction.getBankAccountId())
                            .amountCents(recurringTransaction.getAmountCents()).bankAccountId(targetId)
                            .categoryIds(recurringTransaction.getCategoryIds())
                            .currency(recurringTransaction.getCurrency())
                            .description(recurringTransaction.getDescription())
                            .isActive(recurringTransaction.isActive())
                            .ownerId(recurringTransaction.getOwnerId())
                            .paymentMeanId(recurringTransaction.getPaymentMeanId())
                            .temporalExpressionType(recurringTransaction.getTemporalExpressionType()).build();

            recurringTransactionClient.update(source.getId(), request);
        } catch (io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
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


        try {
            RecurringTransactionResponse recurringTransaction = recurringTransactionClient.getById(sourceId);
            if (recurringTransaction == null ||
                    !recurringTransaction.getOwnerId().equals(authenticationFacade.getCurrentUser().getId())) {
                throw new ResourceNotFoundException("RecurringTransaction not found " + sourceId);
            }

            return bankAccountConverter
                    .convertToResource(bankAccountRepository.findOne(recurringTransaction.getBankAccountId()));

        } catch (io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException e) {
            throw new ResourceNotFoundException("RecurringTransaction not found " + sourceId);
        }
    }

    @Override
    public Iterable<BankAccountResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }
}
