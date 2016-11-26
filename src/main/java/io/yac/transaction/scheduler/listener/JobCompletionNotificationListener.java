package io.yac.transaction.scheduler.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 17/02/2016.
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOG.info("Batch job terminated: " + jobExecution.getJobInstance().getJobName());
        } else {
            LOG.info("Batch job failed: " + jobExecution.getJobInstance().getJobName() + " with status " +
                    jobExecution.getStatus(), jobExecution.getAllFailureExceptions());
        }

    }
}
