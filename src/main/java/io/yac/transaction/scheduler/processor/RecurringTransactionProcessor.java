package io.yac.transaction.scheduler.processor;

import io.yac.transaction.domain.RecurringTransaction;
import io.yac.transaction.domain.Transaction;
import io.yac.transaction.repository.RecurringTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by geoffroy on 17/02/2016.
 */
public class RecurringTransactionProcessor implements ItemProcessor<RecurringTransaction, Transaction> {

    private static final Logger LOG = LoggerFactory.getLogger(RecurringTransactionProcessor.class);

    private final RecurringTransactionRepository recurringTransactionRepository;

    @Autowired
    public RecurringTransactionProcessor(RecurringTransactionRepository recurringTransactionRepository) {
        this.recurringTransactionRepository = recurringTransactionRepository;
    }


    @Override
    public Transaction process(RecurringTransaction item) throws Exception {
        if (item.isOccuringOn(new Date())) {
            LOG.info("Transaction " + item + "is occuring today");
            item.setLastRunOn(new Date());
            recurringTransactionRepository.save(item);

            return Transaction.builder().description(item.getDescription()).owner(item.getOwner())
                    .currency(item.getCurrency()).paymentMean(item.getPaymentMean()).date(new Date())
                    .bankAccount(item.getBankAccount()).amountCents(item.getAmountCents())
                    .categories(item.getCategories()).build();

        }
        LOG.info("Transaction " + item + " is not occuring today");
        return null;


    }
}
