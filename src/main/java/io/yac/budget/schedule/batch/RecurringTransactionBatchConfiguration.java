package io.yac.budget.schedule.batch;

import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.RecurringTransactionRepository;
import io.yac.budget.schedule.batch.processor.RecurringTransactionProcessor;
import io.yac.budget.schedule.batch.writer.RecurringTransactionWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;

/**
 * Created by geoffroy on 17/02/2016.
 */
@Configuration
@EnableBatchProcessing
public class RecurringTransactionBatchConfiguration {

    @Autowired
    RecurringTransactionRepository recurringTransactionRepository;

    @Bean
    public ItemReader<RecurringTransaction> reader() {
        RepositoryItemReader<RecurringTransaction> reader = new RepositoryItemReader<>();
        reader.setRepository(recurringTransactionRepository);
        reader.setMethodName("findAll");
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        reader.setPageSize(100);
        return reader;
    }

    @Bean
    public ItemProcessor<RecurringTransaction, Transaction> processor() {
        return new RecurringTransactionProcessor();
    }

    @Bean
    public ItemWriter<Transaction> writer() {
        return new RecurringTransactionWriter();
    }


    @Bean
    public Job processRecurringTransaction(JobBuilderFactory jobs, Step s1, JobExecutionListener listener) {
        return jobs.get("processRecurringTransaction").incrementer(new RunIdIncrementer()).listener(listener).flow(s1)
                .end().build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<RecurringTransaction> reader,
                      ItemWriter<Transaction> writer, ItemProcessor<RecurringTransaction, Transaction> processor) {
        return stepBuilderFactory.get("step1").<RecurringTransaction, Transaction>chunk(100).reader(reader)
                .processor(processor).writer(writer).build();
    }

}
