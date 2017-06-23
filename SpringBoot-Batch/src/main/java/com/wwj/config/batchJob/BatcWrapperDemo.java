package com.wwj.config.batchJob;

import com.wwj.config.batchJob.listener.JobCompletionNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;

/**
 * spring batch 配置
 *
 * @author
 */
@Configuration
@EnableBatchProcessing
@Component
public class BatcWrapperDemo {

    private static final Logger logger = LoggerFactory.getLogger(BatcWrapperDemo.class);

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Resource
    private JobCompletionNotificationListener jobCompletionNotificationListener;

    public Job initJob(String jobName, JobExecutionListener jobExecutionListener, Step step) {
        return jobs.get(jobName)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(step)
                .build();
    }

    public <T> Step initStep(String stepName, int chunkSize,
                             ItemReader<T> reader, ItemProcessor<T, T> processor,
                             ItemWriter<T> writer) {
        return stepBuilderFactory.get(stepName)
                .<T, T>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
//                .retry(Exception.class)   // 重试
//                .noRetry(ParseException.class)
//                .retryLimit(1)           //每条记录重试一次
//                .listener(new RetryFailuireItemListener())
//                .skip(Exception.class)
//                .skipLimit(10)      //一共允许跳过10次异常
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .throttleLimit(1)        //并发任务数为 10,默认为4。开启多线程候，读取数据后对象转换、处理、写入都开启了多线程处理
                .transactionManager(transactionManager)
                .build();


    }

    public <T> ItemReader initReader(String inputFile,LineMapper<T> objMapper) {
        FlatFileItemReader<T> reader = new FlatFileItemReader<T>();
        reader.setResource(new ClassPathResource(inputFile));
        reader.open(new ExecutionContext());
        reader.setLineMapper(objMapper);
        reader.setLinesToSkip(1);
        return reader;
    }

    public <T> LineMapper initLineMapper(FieldSetMapper<T> objMapper,Class<T> clazz) {
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<T>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"type", "value", "deleteFlag"});

        BeanWrapperFieldSetMapper<T> fieldSetMapper = new BeanWrapperFieldSetMapper<T>();
        fieldSetMapper.setTargetType(clazz);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(objMapper);
        return lineMapper;
    }

}