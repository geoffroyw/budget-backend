package io.yac.budget.schedule.batch.writer;

import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 17/02/2016.
 */
public class RecurringTransactionWriter implements ItemWriter<Transaction> {

    @Autowired
    TransactionRepository repository;

    @Override
    public void write(List<? extends Transaction> items) throws Exception {
        repository.save(items.stream().filter((item) -> item != null)
                .collect(Collectors.toList()));
    }
}
