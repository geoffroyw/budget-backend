package io.yac.scheduler.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by geoffroy on 17/02/2016.
 */
@EnableScheduling
public class JobRunner {

    @Scheduled(cron = "5 0 * * *") //Runs every day 5 minutes after midnight
    public void processRecurringTransaction() {
        SpringApplication.run(RecurringTransactionBatchConfiguration.class);
    }
}
