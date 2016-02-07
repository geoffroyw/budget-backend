package io.yac.budget.api.converter.impl;

import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.Transaction;
import io.yac.budget.domain.TransactionType;
import org.springframework.stereotype.Service;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class TransactionConverter implements ResourceEntityConverter<TransactionResource, Transaction> {
    @Override
    public TransactionResource convertToResource(Transaction entity) {
        return TransactionResource.builder().id(entity.getId()).currency(entity.getCurrency())
                .type(entity.getType().getExternalName()).description(entity.getDescription())
                .amountCents(entity.getAmountCents()).date(entity.getDate()).isConfirmed(entity.isConfirmed()).build();
    }

    @Override
    public Transaction convertToEntity(TransactionResource resource) {
        return Transaction.builder().id(resource.getId()).date(resource.getDate()).currency(resource.getCurrency())
                .amountCents(
                        resource.getAmountCents()).isConfirmed(resource.getConfirmed()).description(
                        resource.getDescription()).type(TransactionType.fromExternalName(resource.getType())).build();
    }
}
