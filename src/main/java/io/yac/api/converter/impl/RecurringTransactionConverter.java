package io.yac.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.api.converter.ResourceEntityConverter;
import io.yac.api.resources.RecurringTransactionResource;
import io.yac.categories.domain.Category;
import io.yac.core.domain.SupportedCurrency;
import io.yac.core.domain.transaction.recurring.RecurringTransaction;
import io.yac.core.repository.BankAccountRepository;
import io.yac.categories.repository.CategoryRepository;
import io.yac.core.repository.PaymentMeanRepository;
import io.yac.core.repository.transaction.RecurringTransactionRepository;
import io.yac.scheduler.expression.TemporalExpression;
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
    CategoryRepository categoryRepository;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;


    public RecurringTransactionConverter() {
    }

    @VisibleForTesting
    RecurringTransactionConverter(RecurringTransactionRepository recurringTransactionRepository,
                                         CategoryRepository categoryRepository,
                                         PaymentMeanRepository paymentMeanRepository,
                                         BankAccountRepository bankAccountRepository) {
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.categoryRepository = categoryRepository;
        this.paymentMeanRepository = paymentMeanRepository;
        this.bankAccountRepository = bankAccountRepository;
    }


    @Override public RecurringTransactionResource convertToResource(RecurringTransaction entity) {
        return RecurringTransactionResource.builder().amountCents(entity.getAmountCents())
                .currency(entity.getCurrency().getExternalName()).description(entity.getDescription())
                .lastRunOn(entity.getLastRunOn()).recurringType(entity.getTemporalExpressionType().getExternalName())
                .categories(entity.getCategories() == null ? null : entity.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toList()))
                .bankAccount(entity.getBankAccount().getId())
                .paymentMean(entity.getPaymentMean().getId())
                .id(entity.getId()).isActive(entity.isActive()).build();
    }

    @Override public RecurringTransaction convertToEntity(RecurringTransactionResource resource, Long id) {
        RecurringTransaction transaction;
        if (id != null) {
            transaction = recurringTransactionRepository.findOne(id);
        } else {
            transaction = new RecurringTransaction();
        }

        transaction.setTemporalExpressionType(
                TemporalExpression.TemporalExpressionType.fromExternalName(resource.getRecurringType()));
        transaction.setCurrency(SupportedCurrency.fromExternalName(resource.getCurrency()));
        transaction.setCategories(resource.getCategories() == null ? null : (List<Category>) categoryRepository
                .findAll(resource.getCategories()));
        transaction.setPaymentMean(resource.getPaymentMean() == null ? null : paymentMeanRepository
                .findOne(resource.getPaymentMean()));
        transaction.setBankAccount(resource.getBankAccount() == null ? null : bankAccountRepository
                .findOne(resource.getBankAccount()));
        transaction.setAmountCents(resource.getAmountCents());
        transaction.setActive(resource.getIsActive());
        transaction.setDescription(resource.getDescription());
        return transaction;
    }
}
