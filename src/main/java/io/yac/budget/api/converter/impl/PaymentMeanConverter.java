package io.yac.budget.api.converter.impl;

import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.domain.PaymentMean;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class PaymentMeanConverter implements ResourceEntityConverter<PaymentMeanResource, PaymentMean> {
    @Override
    public PaymentMeanResource convertToResource(PaymentMean entity) {
        return PaymentMeanResource.builder().id(entity.getId()).name(entity.getName()).currency(entity.getCurrency())
                .build();
    }

    @Override
    public PaymentMean convertToEntity(PaymentMeanResource resource) {
        return PaymentMean.builder().id(resource.getId()).name(resource.getName()).currency(resource.getCurrency())
                .build();
    }
}
