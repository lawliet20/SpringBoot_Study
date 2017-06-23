package com.wwj.config.batchJob.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * Created by sherry on 2017/6/7.
 */
public class MyJobExecutionDecider implements JobExecutionDecider {

    public MyJobExecutionDecider() {
    }

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if(stepExecution.getReadCount() < 1) {
            return new FlowExecutionStatus("FAILED");
        }
        return FlowExecutionStatus.COMPLETED;
    }
}
