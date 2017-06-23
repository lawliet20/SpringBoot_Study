package com.wwj.config.batchJob;

import com.wwj.config.batchJob.domain.Message;
import com.wwj.config.batchJob.domain.PayRecord;
import com.wwj.config.batchJob.listener.JobCompletionNotificationListener;
import com.wwj.config.batchJob.listener.RetryFailuireItemListener;
import com.wwj.config.batchJob.mapper.MessageMapper;
import com.wwj.config.batchJob.mapper.PayRecodeMapper;
import com.wwj.config.batchJob.processor.MessageItemProcessor;
import com.wwj.config.batchJob.processor.PayRecordItemProcessor;
import com.wwj.config.batchJob.writer.MessageItemWriter;
import com.wwj.config.batchJob.writer.PayRecordItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * spring batch 配置
 * 多step的并行执行(step之间业务是独立的，相互间没有影响的)
 * 场景：扣费同时生成扣费通知
 * @author
 */
@Configuration
@EnableBatchProcessing
public class BatchSplitStepConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchSplitStepConfiguration.class);

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Resource
    private JobCompletionNotificationListener jobExeListener;


    private String payRecode="payRecode.txt";
    private String message="message.txt";

    /**
     * 构建job
     */
    @Bean(name = "splitJob")
    public Job importFileJob() throws Exception {
        //扣费
        Flow payRecodeFlow = new FlowBuilder<SimpleFlow>("payRecode")
                .start(payRecodeStep())
                .build();
        //生产扣费通知
        Flow messageFlow = new FlowBuilder<SimpleFlow>("message")
                .start(messageStep())
                .build();
        return (Job) jobs.get("splitJob")
                .incrementer(new RunIdIncrementer())//添加一个自增的值，以区分不同的Job实例，当然，这个值在job的其他的地方并不会用到，仅仅是为了标示不同JobInstance
                .listener(jobExeListener)
                .start(payRecodeFlow)
                .split(new SimpleAsyncTaskExecutor()).add(payRecodeFlow,messageFlow)
                .build();
    }

    /**
     * 声明扣费step(读取单个文件)
     */
    @Bean
    public Step payRecodeStep() throws IOException {
        logger.error("payRecodeStep");
        return stepBuilderFactory.get("payRecodeStep-step")
                .<PayRecord, PayRecord>chunk(10)//每10个记录提交一次事物（在写入数据库时候可以看出区别（串行执行可以看出））
                .reader(payRecordItemReader())
                .processor(payRecodeProcessor())
                .writer(payRecordItemWriter())
                .faultTolerant()
                .retry(Exception.class)   // 重试
                .noRetry(ParseException.class)
                .retryLimit(1)           //每条记录重试一次
                .listener(new RetryFailuireItemListener())
                .skip(Exception.class)
                .skipLimit(10)         //一共允许跳过10次异常
                .taskExecutor(new SimpleAsyncTaskExecutor()) //设置并发方式执行
                .throttleLimit(3)        //并发任务数为 10,默认为4。开启多线程候，读取数据后对象转换、处理、写入都开启了多线程处理
                .transactionManager(transactionManager)
                .build();
    }

    /**
     * 声明扣费信息step(读取单个文件)
     */
    @Bean
    public Step messageStep() throws IOException {
        logger.error("message-step");
        return stepBuilderFactory.get("message-step")
                .<Message, Message>chunk(10)//每10个记录提交一次事物（在写入数据库时候可以看出区别（串行执行可以看出））
                .reader(messageItemReader())
                .processor(messageProcessor())
                .writer(messageItemWriter())
                .faultTolerant()
                .retry(Exception.class)   // 重试
                .noRetry(ParseException.class)
                .retryLimit(1)           //每条记录重试一次
                .listener(new RetryFailuireItemListener())
                .skip(Exception.class)
                .skipLimit(10)         //一共允许跳过10次异常
                .taskExecutor(new SimpleAsyncTaskExecutor()) //设置并发方式执行
                .throttleLimit(3)        //并发任务数为 10,默认为4。开启多线程候，读取数据后对象转换、处理、写入都开启了多线程处理
                .transactionManager(transactionManager)
                .build();
    }

    /**
     * 读取扣费信息
     *
     * @return
     * @throws IOException
     */
    @Bean
    @StepScope
    public ItemReader<PayRecord> payRecordItemReader() throws IOException {
        logger.info("payRecode:" + new ClassPathResource(payRecode).getURL().getPath());
        if (payRecode == null) {
            logger.error("The blacklist reader file is null");
            return null;
        }
        FlatFileItemReader<PayRecord> reader = new FlatFileItemReader<PayRecord>();
        reader.setResource(new ClassPathResource(payRecode));
        reader.open(new ExecutionContext());
        reader.setLineMapper(payRecordLineMapper());
        reader.setLinesToSkip(1);

        return reader;
    }

    /**
     * 扣费通知信息
     *
     * @return
     * @throws IOException
     */
    @Bean
    @StepScope
    public ItemReader<Message> messageItemReader() throws IOException {
        logger.info("payRecode:" + new ClassPathResource(message).getURL().getPath());
        if (message == null) {
            logger.error("The blacklist reader file is null");
            return null;
        }
        FlatFileItemReader<Message> reader = new FlatFileItemReader<Message>();
        reader.setResource(new ClassPathResource(message));
        reader.open(new ExecutionContext());
        reader.setLineMapper(messageLineMapper());
        reader.setLinesToSkip(1);

        return reader;
    }

    /**
     * 读取文本行映射POJO
     * @return
     */
    @Bean
    @StepScope
    public LineMapper<PayRecord> payRecordLineMapper() {
        DefaultLineMapper<PayRecord> lineMapper = new DefaultLineMapper<PayRecord>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"name","fee","paidFees","unpaidFees","payStatus"});

        BeanWrapperFieldSetMapper<PayRecord> fieldSetMapper = new BeanWrapperFieldSetMapper<PayRecord>();
        fieldSetMapper.setTargetType(PayRecord.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new PayRecodeMapper());
        return lineMapper;
    }

    /**
     * 读取文本行映射POJO
     * @return
     */
    @Bean
    @StepScope
    public LineMapper<Message> messageLineMapper() {
        DefaultLineMapper<Message> lineMapper = new DefaultLineMapper<Message>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"name","content"});

        BeanWrapperFieldSetMapper<Message> fieldSetMapper = new BeanWrapperFieldSetMapper<Message>();
        fieldSetMapper.setTargetType(Message.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new MessageMapper());
        return lineMapper;
    }

    /**
     * payRecode处理过程
     * @return
     */
    @Bean
    @StepScope
    public ItemProcessor<PayRecord, PayRecord> payRecodeProcessor() {
        return new PayRecordItemProcessor();
    }

    /**
     * message处理过程
     * @return
     */
    @Bean
    @StepScope
    public ItemProcessor<Message, Message> messageProcessor() {
        return new MessageItemProcessor();
    }

    /**
     * payRecode写出内容
     * @return
     */
    @Bean
    @StepScope
    public ItemWriter<PayRecord> payRecordItemWriter() {
        return new PayRecordItemWriter();
    }

    /**
     * message写出内容
     * @return
     */
    @Bean
    @StepScope
    public ItemWriter<Message> messageItemWriter() {
        return new MessageItemWriter();
    }

}