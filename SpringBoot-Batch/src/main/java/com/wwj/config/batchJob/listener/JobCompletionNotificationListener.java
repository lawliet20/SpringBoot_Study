package com.wwj.config.batchJob.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
  
    @Override  
    public void afterJob(JobExecution jobExecution) {
        System.out.println("job执行后监听");
    }  
  
    @Override  
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("job执行前监听");
    }  
}  