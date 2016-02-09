package io.yac.budget.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class PaymentMeanConverter implements ResourceEntityConverter<PaymentMeanResource, PaymentMean> {

    @Autowired
    TransactionRepository transactionRepository;

    public PaymentMeanConverter() {
    }

    @VisibleForTesting
    PaymentMeanConverter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public PaymentMeanResource convertToResource(PaymentMean entity) {
        return PaymentMeanResource.builder().id(entity.getId()).name(entity.getName()).currency(entity.getCurrency())
                .transactions(entity.getTransactions() == null ? null : entity.getTransactions().stream()
                        .map(this::buildTransactionResource)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public PaymentMean convertToEntity(PaymentMeanResource resource) {
        return PaymentMean.builder().id(resource.getId()).name(resource.getName()).currency(resource.getCurrency())
                .transactions(resource.getTransactions() == null ? null : (List<Transaction>) transactionRepository
                        .findAll(resource.getTransactions().stream().map(TransactionResource::getId).collect(
                                Collectors.toList())))
                .build();
    }

    private TransactionResource buildTransactionResource(Transaction transaction) {
        return TransactionResource.builder().id(transaction.getId()).build();
    }
}
