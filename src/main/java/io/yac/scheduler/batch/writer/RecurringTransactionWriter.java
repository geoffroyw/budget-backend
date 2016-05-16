package io.yac.scheduler.batch.writer;

import io.yac.core.domain.transaction.Transaction;
import io.yac.core.repository.transaction.TransactionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by geoffroy on 17/02/2016.
 */
public class RecurringTransactionWriter implements ItemWriter<Transaction> {

    @Autowired
    TransactionRepository repository;

    @Override
    public void write(List<? extends Transaction> items) throws Exception {
        items.forEach((item) -> repository.save(item));
    }
}
