package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.budget.api.converter.impl.PaymentMeanConverter;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.PaymentMeanRepository;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class TransactionToPaymentMeanEndpoint implements RelationshipRepository<TransactionResource, Long, PaymentMeanResource, Long> {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    PaymentMeanConverter paymentMeanConverter;

    @Override
    public void setRelation(TransactionResource source, Long targetId, String fieldName) {

        Transaction transaction = transactionRepository.findOne(source.getId());
        PaymentMean paymentMean = paymentMeanRepository.findOne(targetId);

        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found " + source.getId());
        }

        if (paymentMean == null) {
            throw new ResourceNotFoundException("Payment mean not found " + targetId);
        }

        transaction.setPaymentMean(paymentMean);

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
    public PaymentMeanResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        Transaction transaction = transactionRepository.findOne(sourceId);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found " + sourceId);
        }
        return paymentMeanConverter.convertToResource(transaction.getPaymentMean());
    }

    @Override
    public Iterable<PaymentMeanResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }
}
