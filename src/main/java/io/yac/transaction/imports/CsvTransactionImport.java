package io.yac.transaction.imports;

import io.yac.transaction.domain.Transaction;
import io.yac.transaction.scheduler.listener.JobCompletionNotificationListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by geoffroy on 25/06/2016.
 */
@Configuration
@EnableBatchProcessing
public class CsvTransactionImport {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public ImportTransactionItemProcessor processor;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<CsvTransaction> reader() {
        FlatFileItemReader<CsvTransaction> reader = new FlatFileItemReader<CsvTransaction>();
        reader.setResource(new ClassPathResource("sample-data.csv")); //TODO
        reader.setLineMapper(new DefaultLineMapper<CsvTransaction>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"date", "name", "incomeAmount", "expenseAmount", "isConfirmed", "paymentMeanName",
                                      "currency", "bankAccount", "category"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<CsvTransaction>() {{
                setTargetType(CsvTransaction.class);
            }});
        }});
        return reader;
    }

    @Bean
    public HibernateItemWriter<Transaction> writer() {
        return new HibernateItemWriter<>();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importTransactionJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<CsvTransaction, Transaction>chunk(10)
                .reader(reader())
                .processor(processor)
                .writer(writer())
                .build();
    }


}
