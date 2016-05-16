package io.yac.api.converter.impl;

import com.google.common.annotations.VisibleForTesting;
import io.yac.api.converter.ResourceEntityConverter;
import io.yac.api.resources.TransactionResource;
import io.yac.core.domain.Category;
import io.yac.core.domain.SupportedCurrency;
import io.yac.core.domain.transaction.Transaction;
import io.yac.core.repository.BankAccountRepository;
import io.yac.core.repository.CategoryRepository;
import io.yac.core.repository.PaymentMeanRepository;
import io.yac.core.repository.transaction.TransactionRepository;
import io.yac.services.clients.rate.RateConversionClient;
import io.yac.services.clients.rate.RateConversionClient.RateConversionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    RateConversionClient rateConversionClient;

    public TransactionConverter() {
    }

    @VisibleForTesting
    TransactionConverter(BankAccountRepository bankAccountRepository, PaymentMeanRepository paymentMeanRepository,
                         CategoryRepository categoryRepository, TransactionRepository transactionRepository,
                         RateConversionClient rateConversionClient) {
        this.bankAccountRepository = bankAccountRepository;
        this.paymentMeanRepository = paymentMeanRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.rateConversionClient = rateConversionClient;
    }


    @Override
    public TransactionResource convertToResource(Transaction entity) {
        Integer settlementAmount = entity.getSettlementAmountCents();

        String settlementCurrency;
        if (entity.getCurrency() == entity.getPaymentMean().getCurrency()) {
            settlementAmount = entity.getAmountCents();
            settlementCurrency = entity.getCurrency().getExternalName();
        } else {
            settlementCurrency =
                    entity.getSettlementCurrency() == null ? entity.getPaymentMean().getCurrency().getExternalName()
                                                           : entity.getSettlementCurrency().getExternalName();

            if (settlementAmount == null) {
                RateConversionResponse rateConversionResponseResponse = rateConversionClient
                        .convert(new BigDecimal(entity.getAmountCents() / 100), entity.getCurrency().getExternalName(),
                                settlementCurrency);
                if (rateConversionResponseResponse != null) {
                    BigDecimal amountInPaymentMeanCurrency = rateConversionResponseResponse.getAmountInToCurrency();

                    settlementAmount =
                            amountInPaymentMeanCurrency.multiply(new BigDecimal("100")).toBigInteger().intValue();
                }

            }
        }


        return TransactionResource.builder().id(entity.getId()).currency(entity.getCurrency().getExternalName())
                .paymentMean(entity.getPaymentMean().getId())
                .settlementCurrency(
                        settlementCurrency)
                .settlementAmountCents(settlementAmount)
                .isSettlementAmountIndicative(entity.getSettlementAmountCents() == null)
                .description(entity.getDescription())
                .categories(entity.getCategories() == null ? null : entity.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toList()))
                .amountCents(entity.getAmountCents()).date(entity.getDate()).isConfirmed(entity.isConfirmed())
                .bankAccount(entity.getBankAccount().getId()).build();
    }

    @Override
    public Transaction convertToEntity(TransactionResource resource, Long id) {
        Transaction transaction;
        if (id != null) {
            transaction = transactionRepository.findOne(id);
        } else {
            transaction = new Transaction();
        }

        transaction.setDate(resource.getDate());
        transaction.setCurrency(SupportedCurrency.fromExternalName(resource.getCurrency()));
        transaction.setCategories(resource.getCategories() == null ? null : (List<Category>) categoryRepository
                .findAll(resource.getCategories()));
        transaction.setPaymentMean(resource.getPaymentMean() == null ? null : paymentMeanRepository
                .findOne(resource.getPaymentMean()));
        transaction.setBankAccount(resource.getBankAccount() == null ? null : bankAccountRepository
                .findOne(resource.getBankAccount()));
        transaction.setSettlementAmountCents(resource.getSettlementAmountCents());
        transaction.setSettlementCurrency(SupportedCurrency.fromExternalName(resource.getSettlementCurrency()));
        transaction.setAmountCents(resource.getAmountCents());
        transaction.setConfirmed(resource.getIsConfirmed());
        transaction.setDescription(resource.getDescription());
        return transaction;
    }
}
