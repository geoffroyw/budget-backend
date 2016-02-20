package io.yac.budget.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.budget.api.converter.ResourceEntityConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.CategoryResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.Category;
import io.yac.budget.domain.SupportedCurrency;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.CategoryRepository;
import io.yac.budget.repository.PaymentMeanRepository;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class TransactionConverter implements ResourceEntityConverter<TransactionResource, Transaction> {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public TransactionConverter() {
    }

    @VisibleForTesting
    TransactionConverter(BankAccountRepository bankAccountRepository, PaymentMeanRepository paymentMeanRepository,
                         CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.paymentMeanRepository = paymentMeanRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResource convertToResource(Transaction entity) {
        return TransactionResource.builder().id(entity.getId()).currency(entity.getCurrency().getExternalName())
                .paymentMean(PaymentMeanResource.builder().id(entity.getPaymentMean().getId()).build())
                .description(entity.getDescription())
                .categories(entity.getCategories() == null ? null : entity.getCategories().stream()
                        .map(category -> CategoryResource.builder().id(category.getId()).build())
                        .collect(Collectors.toList()))
                .amountCents(entity.getAmountCents()).date(entity.getDate()).isConfirmed(entity.isConfirmed())
                .bankAccount(
                        BankAccountResource.builder().id(entity.getBankAccount().getId()).build()).build();
    }

    @Override
    public Transaction convertToEntity(TransactionResource resource) {
        Transaction transaction;
        if (resource.getId() != null) {
            transaction = transactionRepository.findOne(resource.getId());
        } else {
            transaction = new Transaction();
        }

        transaction.setDate(resource.getDate());
        transaction.setCurrency(SupportedCurrency.fromExternalName(resource.getCurrency()));
        transaction.setCategories(resource.getCategories() == null ? null : (List<Category>) categoryRepository
                .findAll(resource.getCategories().stream().map(CategoryResource::getId).collect(Collectors.toList())));
        transaction.setPaymentMean(resource.getPaymentMean() == null ? null : paymentMeanRepository
                .findOne(resource.getPaymentMean().getId()));
        transaction.setBankAccount(resource.getBankAccount() == null ? null : bankAccountRepository
                .findOne(resource.getBankAccount().getId()));

        transaction.setAmountCents(resource.getAmountCents());
        transaction.setConfirmed(resource.getIsConfirmed());
        transaction.setDescription(resource.getDescription());
        return transaction;
    }
}
