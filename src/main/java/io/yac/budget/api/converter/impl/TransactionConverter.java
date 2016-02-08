package io.yac.budget.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.Transaction;
import io.yac.budget.domain.TransactionType;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.PaymentMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class TransactionConverter implements ResourceEntityConverter<TransactionResource, Transaction> {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    public TransactionConverter() {
    }

    @VisibleForTesting
    TransactionConverter(BankAccountRepository bankAccountRepository, PaymentMeanRepository paymentMeanRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.paymentMeanRepository = paymentMeanRepository;
    }

    @Override
    public TransactionResource convertToResource(Transaction entity) {
        return TransactionResource.builder().id(entity.getId()).currency(entity.getCurrency())
                .paymentMean(PaymentMeanResource.builder().id(entity.getPaymentMean().getId()).build())
                .type(entity.getType().getExternalName()).description(entity.getDescription())
                .amountCents(entity.getAmountCents()).date(entity.getDate()).isConfirmed(entity.isConfirmed())
                .bankAccount(
                        BankAccountResource.builder().id(entity.getBankAccount().getId()).build()).build();
    }

    @Override
    public Transaction convertToEntity(TransactionResource resource) {
        return Transaction.builder().id(resource.getId()).date(resource.getDate()).currency(resource.getCurrency())
                .bankAccount(resource.getBankAccount() == null ? null : bankAccountRepository
                        .findOne(resource.getBankAccount().getId()))
                .paymentMean(resource.getPaymentMean() == null ? null : paymentMeanRepository
                        .findOne(resource.getPaymentMean().getId()))
                .amountCents(
                        resource.getAmountCents()).isConfirmed(resource.getConfirmed()).description(
                        resource.getDescription()).type(TransactionType.fromExternalName(resource.getType())).build();
    }
}
