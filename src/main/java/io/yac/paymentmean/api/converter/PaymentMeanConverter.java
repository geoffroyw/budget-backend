package io.yac.paymentmean.api.converter;

import com.google.common.annotations.VisibleForTesting;
import io.yac.api.converter.ResourceEntityConverter;
import io.yac.paymentmean.api.PaymentMeanResource;
import io.yac.paymentmean.domain.PaymentMean;
import io.yac.core.domain.SupportedCurrency;
import io.yac.transaction.domain.Transaction;
import io.yac.paymentmean.repository.PaymentMeanRepository;
import io.yac.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class PaymentMeanConverter implements ResourceEntityConverter<PaymentMeanResource, PaymentMean> {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    public PaymentMeanConverter() {
    }

    @VisibleForTesting
    PaymentMeanConverter(TransactionRepository transactionRepository, PaymentMeanRepository paymentMeanRepository) {
        this.transactionRepository = transactionRepository;
        this.paymentMeanRepository = paymentMeanRepository;
    }

    @Override
    public PaymentMeanResource convertToResource(PaymentMean entity) {
        return PaymentMeanResource.builder().id(entity.getId()).name(entity.getName())
                .currency(entity.getCurrency().getExternalName())
                .transactions(entity.getTransactions() == null ? null : entity.getTransactions().stream()
                        .map(Transaction::getId)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public PaymentMean convertToEntity(PaymentMeanResource resource, Long id) {
        PaymentMean paymentMean;
        if (id == null) {
            paymentMean = new PaymentMean();
        } else {
            paymentMean = paymentMeanRepository.findOne(id);
        }

        paymentMean.setName(resource.getName());
        paymentMean.setCurrency(SupportedCurrency.fromExternalName(resource.getCurrency()));
        paymentMean
                .setTransactions(resource.getTransactions() == null ? null : (List<Transaction>) transactionRepository
                        .findAll(resource.getTransactions()));


        return paymentMean;
    }

}
