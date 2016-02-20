package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.PaymentMeanConverter;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.repository.PaymentMeanRepository;
import io.yac.budget.repository.RecurringTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class RecurringTransactionToPaymentMeanEndpoint implements RelationshipRepository<RecurringTransactionResource, Long, PaymentMeanResource, Long> {
    @Autowired
    RecurringTransactionRepository RecurringTransactionRepository;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    PaymentMeanConverter paymentMeanConverter;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Override
    public void setRelation(RecurringTransactionResource source, Long targetId, String fieldName) {

        RecurringTransaction RecurringTransaction =
                RecurringTransactionRepository
                        .findOneByOwnerAndId(authenticationFacade.getCurrentUser(), source.getId());
        PaymentMean paymentMean =
                paymentMeanRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), targetId);

        if (RecurringTransaction == null) {
            throw new ResourceNotFoundException("RecurringTransaction not found " + source.getId());
        }

        if (paymentMean == null) {
            throw new ResourceNotFoundException("Payment mean not found " + targetId);
        }

        RecurringTransaction.setPaymentMean(paymentMean);

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
    public PaymentMeanResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        RecurringTransaction RecurringTransaction =
                RecurringTransactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), sourceId);
        if (RecurringTransaction == null) {
            throw new ResourceNotFoundException("RecurringTransaction not found " + sourceId);
        }
        return paymentMeanConverter.convertToResource(RecurringTransaction.getPaymentMean());
    }

    @Override
    public Iterable<PaymentMeanResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }
}
