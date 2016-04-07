package io.yac.budget.api.converter.impl;

import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.recurring.transactions.client.RecurringTransactionRequest;
import io.yac.budget.recurring.transactions.client.resources.RecurringTransactionResponse;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
                        response.getCategoryIds() == null ? null : response.getCategoryIds().stream()
                                .map((c) -> CategoryResource.builder().id(c).build())
                                .collect(Collectors.toList()))
                .bankAccount(response.getBankAccountId() == null ? null : BankAccountResource.builder()
                        .id(response.getBankAccountId()).build())
                .paymentMean(response.getPaymentMeanId() == null ? null : PaymentMeanResource.builder()
                        .id(response.getPaymentMeanId()).build())
                .id(response.getId()).isActive(response.isActive()).build();
    }


    public RecurringTransactionRequest buildRequest(RecurringTransactionResource resource, Long ownerId) {
        return RecurringTransactionRequest.builder().isActive(resource.getIsActive()).description(
                resource.getDescription()).currency(resource.getCurrency()).amountCents(resource.getAmountCents())
                .temporalExpressionType(resource.getRecurringType())
                .categoryIds(resource.getCategories() == null ? null : resource.getCategories().stream()
                        .map(CategoryResource::getId).collect(Collectors.toList()))
                .paymentMeanId(resource.getPaymentMean() == null ? null : resource.getPaymentMean().getId())
                .bankAccountId(resource.getBankAccount() == null ? null : resource.getBankAccount().getId())
                .ownerId(ownerId)
                .build();


    }

    public RecurringTransactionRequest buildRequest(RecurringTransactionResponse response) {
        return RecurringTransactionRequest.builder().isActive(response.isActive()).description(
                response.getDescription()).currency(response.getCurrency()).amountCents(response.getAmountCents())
                .temporalExpressionType(response.getTemporalExpressionType())
                .categoryIds(response.getCategoryIds()).paymentMeanId(response.getPaymentMeanId())
                .bankAccountId(response.getBankAccountId())
                .ownerId(response.getOwnerId())
                .build();


    }

}
