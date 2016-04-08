package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.recurring.transactions.client.RecurringTransactionRequest;
import io.yac.budget.recurring.transactions.client.resources.RecurringTransactionResponse;
import org.springframework.stereotype.Service;

/**
 * Created by geoffroy on 19/02/2016.
 */
@Service
public class RecurringTransactionConverter {


    public RecurringTransactionResource convertToResource(RecurringTransactionResponse response) {
        return RecurringTransactionResource.builder().amountCents(response.getAmountCents())
                .currency(response.getCurrency()).description(response.getDescription())
                .lastRunOn(response.getLastRunOn())
                .recurringType(response.getTemporalExpressionType()).categories(
                        response.getCategoryIds())
                .bankAccount(response.getBankAccountId())
                .paymentMean(response.getPaymentMeanId())
                .id(response.getId()).isActive(response.isActive()).build();
    }


    public RecurringTransactionRequest buildRequest(RecurringTransactionResource resource, Long ownerId) {
        return RecurringTransactionRequest.builder().isActive(resource.getIsActive()).description(
                resource.getDescription()).currency(resource.getCurrency()).amountCents(resource.getAmountCents())
                .temporalExpressionType(resource.getRecurringType())
                .categoryIds(resource.getCategories())
                .paymentMeanId(resource.getPaymentMean())
                .bankAccountId(resource.getBankAccount())
                .ownerId(ownerId)
                .build();


    }

}
