package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.budget.api.converter.impl.TransactionConverter;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.PaymentMeanRepository;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Component
public class PaymentMeanToTransactionEndpoint implements RelationshipRepository<PaymentMeanResource, Long, TransactionResource, Long> {

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionConverter transactionConverter;

    @Override
    public void setRelation(PaymentMeanResource source, Long targetId, String fieldName) {

    }

    @Override
    public void setRelations(PaymentMeanResource source, Iterable<Long> targetIds, String fieldName) {
        PaymentMean paymentMean = getPaymentMean(source);

        paymentMean.setTransactions((List<Transaction>) transactionRepository.findAll(targetIds));

        paymentMeanRepository.save(paymentMean);
    }

    @Override
    public void addRelations(PaymentMeanResource source, Iterable<Long> targetIds, String fieldName) {
        PaymentMean paymentMean = getPaymentMean(source);

        paymentMean.getTransactions().addAll((List<Transaction>) transactionRepository.findAll(targetIds));

        paymentMeanRepository.save(paymentMean);


    }

    private PaymentMean getPaymentMean(PaymentMeanResource source) {
        return getPaymentMean(source.getId());
    }

    private PaymentMean getPaymentMean(Long id) {
        PaymentMean paymentMean = paymentMeanRepository.findOne(id);

        if (paymentMean == null) {
            throw new ResourceNotFoundException("Payment mean not found " + id);
        }
        return paymentMean;
    }

    @Override
    public void removeRelations(PaymentMeanResource source, Iterable<Long> targetIds, String fieldName) {
        PaymentMean paymentMean = getPaymentMean(source);

        paymentMean.getTransactions().removeAll((List<Transaction>) transactionRepository.findAll(targetIds));

        paymentMeanRepository.save(paymentMean);

    }

    @Override
    public TransactionResource findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        return null;
    }

    @Override
    public Iterable<TransactionResource> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        PaymentMean paymentMean = getPaymentMean(sourceId);
        return paymentMean.getTransactions().stream().map(t -> transactionConverter.convertToResource(t)).collect(
                Collectors.toList());
    }
}
