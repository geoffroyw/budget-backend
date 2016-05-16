package io.yac.api.factory;

import io.yac.auth.user.model.User;
import io.yac.core.domain.SupportedCurrency;
import io.yac.core.domain.transaction.Transaction;
import io.yac.core.repository.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by geoffroy on 14/02/2016.
 */
@Component
public class TransactionFactory {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BankAccountFactory bankAccountFactory;

    @Autowired
    PaymentMeanFactory paymentMeanFactory;

    public Transaction saveTransaction(User owner) {
        return transactionRepository
                .save(Transaction.builder().currency(SupportedCurrency.EUR).date(new Date()).amountCents(100)
                        .isConfirmed(false)
                        .description("any")
                        .bankAccount(bankAccountFactory.saveBankAccount(owner))
                        .paymentMean(paymentMeanFactory.savePaymentMean(owner))
                        .owner(owner).build());
    }
}
