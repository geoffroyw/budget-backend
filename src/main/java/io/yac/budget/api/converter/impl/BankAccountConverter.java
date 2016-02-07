package io.yac.budget.api.converter.impl;

import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.domain.BankAccount;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class BankAccountConverter implements ResourceEntityConverter<BankAccountResource, BankAccount> {

    @Override
    public BankAccountResource convertToResource(BankAccount entity) {
        return BankAccountResource.builder().id(entity.getId()).currency(entity.getCurrency()).name(entity.getName())
                .build();
    }

    @Override
    public BankAccount convertToEntity(BankAccountResource resource) {
        return BankAccount.builder().id(resource.getId()).currency(resource.getCurrency()).name(resource.getName())
                .build();
    }
}
