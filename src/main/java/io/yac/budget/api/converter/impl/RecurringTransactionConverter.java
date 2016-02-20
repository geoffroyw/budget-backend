package io.yac.budget.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.Category;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.CategoryRepository;
import io.yac.budget.repository.PaymentMeanRepository;
import io.yac.budget.repository.RecurringTransactionRepository;
import io.yac.budget.schedule.temporal.expression.TemporalExpression.TemporalExpressionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 19/02/2016.
 */
@Service
public class RecurringTransactionConverter implements ResourceEntityConverter<RecurringTransactionResource, RecurringTransaction> {

    @Autowired
    RecurringTransactionRepository recurringTransactionRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @VisibleForTesting
    RecurringTransactionConverter(BankAccountRepository bankAccountRepository,
                                  PaymentMeanRepository paymentMeanRepository,
                                  CategoryRepository categoryRepository,
                                  RecurringTransactionRepository recurringTransactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.paymentMeanRepository = paymentMeanRepository;
        this.categoryRepository = categoryRepository;
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    public RecurringTransactionConverter() {
    }

    @Override
    public RecurringTransactionResource convertToResource(RecurringTransaction entity) {
        return RecurringTransactionResource.builder().amountCents(entity.getAmountCents())
                .currency(entity.getCurrency()).description(entity.getDescription()).lastRunOn(entity.getLastRunOn())
                .recurringType(entity.getTemporalExpressionType().getExternalName()).categories(
                        entity.getCategories() == null ? null : entity.getCategories().stream()
                                .map((c) -> CategoryResource.builder().id(c.getId()).build())
                                .collect(Collectors.toList()))
                .bankAccount(entity.getBankAccount() == null ? null : BankAccountResource.builder()
                        .id(entity.getBankAccount().getId()).build())
                .paymentMean(entity.getPaymentMean() == null ? null : PaymentMeanResource.builder()
                        .id(entity.getPaymentMean().getId()).build())
                .id(entity.getId()).isActive(entity.isActive()).build();
    }

    @Override
    public RecurringTransaction convertToEntity(RecurringTransactionResource resource) {
        RecurringTransaction recurringTransaction;
        if (resource.getId() != null) {
            recurringTransaction = recurringTransactionRepository.findOne(resource.getId());
        } else {
            recurringTransaction = new RecurringTransaction();
        }
        recurringTransaction.setActive(resource.getIsActive());
        recurringTransaction.setDescription(resource.getDescription());
        recurringTransaction.setCurrency(resource.getCurrency());
        recurringTransaction.setAmountCents(resource.getAmountCents());
        recurringTransaction
                .setTemporalExpressionType(TemporalExpressionType.fromExternalName(resource.getRecurringType()));
        recurringTransaction.setCategories(resource.getCategories() == null ? null : (List<Category>) categoryRepository
                .findAll(resource.getCategories().stream().map(CategoryResource::getId).collect(Collectors.toList())));
        recurringTransaction.setPaymentMean(resource.getPaymentMean() == null ? null : paymentMeanRepository
                .findOne(resource.getPaymentMean().getId()));
        recurringTransaction.setBankAccount(resource.getBankAccount() == null ? null : bankAccountRepository
                .findOne(resource.getBankAccount().getId()));


        return recurringTransaction;
    }
}
