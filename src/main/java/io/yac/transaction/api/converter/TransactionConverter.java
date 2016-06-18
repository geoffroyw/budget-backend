package io.yac.transaction.api.converter;

import com.google.common.annotations.VisibleForTesting;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.categories.domain.Category;
import io.yac.categories.repository.CategoryRepository;
import io.yac.common.api.converter.ResourceEntityConverter;
import io.yac.common.domain.SupportedCurrency;
import io.yac.paymentmean.repository.PaymentMeanRepository;
import io.yac.rates.RateConversionResponse;
import io.yac.rates.RateConversionService;
import io.yac.rates.exceptions.NoRateException;
import io.yac.rates.exceptions.UnknownCurrencyException;
import io.yac.transaction.api.TransactionResource;
import io.yac.transaction.domain.Transaction;
import io.yac.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class TransactionConverter implements ResourceEntityConverter<TransactionResource, Transaction> {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionConverter.class);

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RateConversionService rateConversionService;

    public TransactionConverter() {
    }

    @VisibleForTesting TransactionConverter(BankAccountRepository bankAccountRepository,
                                            PaymentMeanRepository paymentMeanRepository,
                                            CategoryRepository categoryRepository,
                                            TransactionRepository transactionRepository,
                                            RateConversionService rateConversionService) {
        this.bankAccountRepository = bankAccountRepository;
        this.paymentMeanRepository = paymentMeanRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.rateConversionService = rateConversionService;
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
                RateConversionResponse rateConversionResponseResponse;
                try {
                    rateConversionResponseResponse = rateConversionService
                            .convert(new BigDecimal(entity.getAmountCents() / 100),
                                    entity.getCurrency().getExternalName(),
                                    settlementCurrency);
                } catch (UnknownCurrencyException | NoRateException e) {
                    rateConversionResponseResponse = null;
                    LOG.error("Exception while converting amount", e);
                }
                if (rateConversionResponseResponse != null) {
                    BigDecimal amountInPaymentMeanCurrency = rateConversionResponseResponse.getAmountInToCurrency();

                    settlementAmount =
                            amountInPaymentMeanCurrency.multiply(new BigDecimal("100")).toBigInteger().intValue();
                }

            }
        }


        return TransactionResource.builder().id(entity.getId()).currency(entity.getCurrency().getExternalName())
                .paymentMean(entity.getPaymentMean().getId())
                .settlementCurrency(settlementCurrency)
                .settlementAmountCents(settlementAmount)
                .isSettlementAmountIndicative(entity.getSettlementAmountCents() == null)
                .description(entity.getDescription())
                .category(entity.getCategories() == null || entity.getCategories().isEmpty() ? null
                                                                                             : entity.getCategories()
                                  .get(0).getId())
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
        transaction.setCategories(resource.getCategory() == null ? null : (List<Category>) categoryRepository
                .findAll(Collections.singletonList(resource.getCategory())));
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
