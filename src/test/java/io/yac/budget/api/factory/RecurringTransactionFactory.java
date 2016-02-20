package io.yac.budget.api.factory;

import io.yac.auth.user.model.User;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.domain.SupportedCurrency;
import io.yac.budget.repository.RecurringTransactionRepository;
import io.yac.budget.schedule.temporal.expression.TemporalExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 19/02/2016.
 */
@Component
public class RecurringTransactionFactory {
    @Autowired
    RecurringTransactionRepository transactionRepository;

    @Autowired
    BankAccountFactory bankAccountFactory;

    @Autowired
    PaymentMeanFactory paymentMeanFactory;

    public RecurringTransaction saveRecurringTransaction(User owner) {
        return transactionRepository
                .save(RecurringTransaction.builder().currency(SupportedCurrency.EUR).temporalExpressionType(
                        TemporalExpression.TemporalExpressionType.DAILY).amountCents(100).description("any")
                        .bankAccount(bankAccountFactory.saveBankAccount(owner))
                        .paymentMean(paymentMeanFactory.savePaymentMean(owner))
                        .owner(owner).build());
    }
}
