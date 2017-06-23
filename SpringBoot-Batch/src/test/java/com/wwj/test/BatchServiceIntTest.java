package com.wwj.test;

import com.wwj.AppMain;
import com.wwj.config.batchJob.*;
import com.wwj.config.batchJob.domain.BlackListDO;
import com.wwj.config.batchJob.listener.JobCompletionNotificationListener;
import com.wwj.config.batchJob.mapper.BlackListFieldSetMapper;
import com.wwj.config.batchJob.processor.BlackListDOItemProcessor;
import com.wwj.config.batchJob.writer.BlackListItemWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for the UserResource REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppMain.class)
public class BatchServiceIntTest {

    private static final Logger log = LoggerFactory.getLogger(SpringBootTest.class);

    @Resource
    private JobLauncher launcher;

    @Resource(name = "fileJob")
    private Job fileJob;

    @Resource(name = "filesJob")
    private Job filesJob;

    @Resource(name = "flowJob")
    private Job flowJob;

    @Resource(name = "jdbcJob")
    private Job jdbcJob;

    @Resource(name = "deciderJob")
    private Job deciderJob;

    @Resource
    private BatcWrapperDemo batcWrapperDemo;

    /*
    * 读取单个txt外部文件(单step的并发执行)
    * */
    @Test
    public void test() {
        try {
//            Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
//            parameters.put("inputFileBlack", new JobParameter("testDate1.txt2"));
            JobExecution je = launcher.run(fileJob, new JobParameters());
            printMessage(je);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 读取多个个txt外部文件
    * */
    @Test
    public void test2() {
        try {
//            Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
//            parameters.put("inputFileBlack", new JobParameter("testDate1.txt;testDate2.txt"));
            JobExecution je = launcher.run(filesJob, new JobParameters());
            printMessage(je);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据库（
     */
    @Test
    public void test3() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobRestartException {
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("testData", new JobParameter("test"));
        JobExecution je = launcher.run(jdbcJob, new JobParameters());
        printMessage(je);
    }

    /*
    * 多step的job
    * */
    @Test
    public void test9() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobRestartException {
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("testData", new JobParameter("test"));
        JobExecution je = launcher.run(flowJob, new JobParameters());
        printMessage(je);
    }

    /*
    * decider分支流程
    * */
    public void test4() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobExecution je = launcher.run(deciderJob, new JobParameters());
        printMessage(je);
    }

    /**
     * 多step的并发执行
     *
     */
    public void test8(){

    }



    /*
    * Remote chunking
    * */
    public void test5() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        //TODO
    }

    /*
    * Partitioning step
    * */
    public void test6() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        //TODO
    }

    /**
     * 封装测试
     */
    @Test
    public void test7() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        LineMapper lineMapper = batcWrapperDemo.initLineMapper(new BlackListFieldSetMapper(), BlackListDO.class);
        ItemReader reader = batcWrapperDemo.initReader("testDate1.txt", lineMapper);
        ItemWriter writer = new BlackListItemWriter();
        ItemProcessor processor = new BlackListDOItemProcessor();

        Step step = batcWrapperDemo.initStep("testStep", 1, reader, processor, writer);

        JobCompletionNotificationListener listener = new JobCompletionNotificationListener();
        Job testJob = batcWrapperDemo.initJob("testJob",listener,step);
        JobExecution je = launcher.run(testJob, new JobParameters());
        printMessage(je);
    }

    private void printMessage(JobExecution je) {
        log.info("Job 的运行记录：{}", je);
        log.info("Job 的运行实例：{}", je.getJobInstance());
        log.info("任务运行过程中的相关信息：{}", je.getExecutionContext());
        log.info("Job 的运行参数：{}", je.getJobParameters());
        log.info(" Step 的运行记录：{}", je.getStepExecutions());
    }

}
