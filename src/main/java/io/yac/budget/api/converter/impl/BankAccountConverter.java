package io.yac.budget.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.BankAccount;
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
public class BankAccountConverter implements ResourceEntityConverter<BankAccountResource, BankAccount> {

    @Autowired
    TransactionRepository transactionRepository;

    public BankAccountConverter() {
    }

    @VisibleForTesting
    BankAccountConverter(TransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    @Override
    public BankAccountResource convertToResource(BankAccount entity) {
        return BankAccountResource.builder().id(entity.getId()).currency(entity.getCurrency()).name(entity.getName())
                .transactions(entity.getTransactions() == null ? null :
                              entity.getTransactions().stream()
                                      .map(t -> TransactionResource.builder().id(t.getId()).build())
                                      .collect(Collectors.toList()))
                .build();
    }

    @Override
    public BankAccount convertToEntity(BankAccountResource resource) {
        return BankAccount.builder().id(resource.getId()).currency(resource.getCurrency()).name(resource.getName())
                .transactions(resource.getTransactions() == null ? null : (List<Transaction>) transactionRepository
                        .findAll(resource.getTransactions().stream().map(TransactionResource::getId).collect(
                                Collectors.toList())))
                .build();
    }
}
