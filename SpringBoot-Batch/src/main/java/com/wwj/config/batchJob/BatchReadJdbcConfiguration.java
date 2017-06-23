package com.wwj.config.batchJob;

import com.wwj.config.batchJob.domain.User;
import com.wwj.config.batchJob.listener.JobCompletionNotificationListener;
import com.wwj.config.batchJob.listener.RetryFailuireItemListener;
import com.wwj.config.batchJob.mapper.UserRowMapper;
import com.wwj.config.batchJob.processor.UserProcessor;
import com.wwj.config.batchJob.writer.UserItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * spring batch 配置
 *
 * @author
 */
@Configuration
@EnableBatchProcessing
public class BatchReadJdbcConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchReadJdbcConfiguration.class);

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Resource(name = "importFileJob-step1")
    private Step readFileStep;

    @Resource
    private DataSource dataSource;

    @Resource
    private JobCompletionNotificationListener jobCompletionNotificationListener;

    /**
     * 构建job
     */
    @Bean(name = "jdbcJob")
    public Job flowJob() throws IOException {
        return jobs.get("jdbcJob")
                .incrementer(new RunIdIncrementer())//添加一个自增的值，以区分不同的Job实例，当然，这个值在job的其他的地方并不会用到，仅仅是为了标示不同JobInstance
                .listener(jobCompletionNotificationListener)
                .start(readFileStep).next(step1())
                .build();
    }

    /**
     * 构建多step的job
     */
    @Bean(name = "flowJob")
    public Job stepsJob() throws IOException {
        return jobs.get("flowJob")
                .incrementer(new RunIdIncrementer())//添加一个自增的值，以区分不同的Job实例，当然，这个值在job的其他的地方并不会用到，仅仅是为了标示不同JobInstance
                .listener(jobCompletionNotificationListener)
                .start(readFileStep).next(step1())
                .build();
    }

    /**
     * 声明step(读取数据库)
     */
    @Bean(name = "flowJob-step1")
    public Step step1() throws IOException {
        logger.error("step1");
        return stepBuilderFactory.get("flowJob-step1")
                .<User, User>chunk(1)//每10个记录提交一次事物（在写入数据库时候可以看出区别（串行执行可以看出））
                .reader(readerJdbc())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .retry(Exception.class)   // 重试
                .noRetry(ParseException.class)
                .retryLimit(1)           //每条记录重试一次
                .listener(new RetryFailuireItemListener())
                .skip(Exception.class)
                .skipLimit(10)      //一共允许跳过10次异常
                .taskExecutor(new SimpleAsyncTaskExecutor()) //设置并发方式执行
                .throttleLimit(1)        //并发任务数为 10,默认为4。开启多线程候，读取数据后对象转换、处理、写入都开启了多线程处理
                .transactionManager(transactionManager)
                .build();
    }

    /**
     * 读取数据库
     * @return
     * @throws IOException
     */
    @Bean(name = "flowJob-step1-read")
    @StepScope
    public ItemReader<User> readerJdbc() {
        JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<User>();
        String sql = "select id,name,age,balance from users ";
        reader.setSql(sql);
        reader.setDataSource(dataSource);
        reader.open(new ExecutionContext());
        reader.setRowMapper(rowMapper());
        return reader;
    }

    /**
     * 读取文本行映射POJO
     * @return
     */
    @Bean
    public UserRowMapper rowMapper(){
        return new UserRowMapper();
    }

    /**
     * 处理过程
     * @return
     */
    @Bean(name = "flowJob-step1-processor")
    @StepScope
    public ItemProcessor<User, User> processor() {
        return new UserProcessor();
    }

    /**
     * 写出内容
     * @return
     */
    @Bean(name = "flowJob-step1-writer")
    @StepScope
    public ItemWriter<User> writer() {
        return new UserItemWriter();
    }

}