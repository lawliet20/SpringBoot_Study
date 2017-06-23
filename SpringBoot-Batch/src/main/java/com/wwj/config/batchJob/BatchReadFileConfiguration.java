package com.wwj.config.batchJob;

import com.wwj.config.batchJob.domain.BlackListDO;
import com.wwj.config.batchJob.listener.JobCompletionNotificationListener;
import com.wwj.config.batchJob.listener.LogProcessListener;
import com.wwj.config.batchJob.listener.RetryFailuireItemListener;
import com.wwj.config.batchJob.mapper.BlackListFieldSetMapper;
import com.wwj.config.batchJob.processor.BlackListDOItemProcessor;
import com.wwj.config.batchJob.writer.BlackListItemWriter;
import com.xiaoleilu.hutool.util.StrUtil;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

/**
 * spring batch 配置
 *
 * @author
 */
@Configuration
@EnableBatchProcessing
public class BatchReadFileConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchReadFileConfiguration.class);

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private PlatformTransactionManager transactionManager;

//    @Value("#{jobParameters[inputFileBlack]}")  String inputFile;

    private String inputFile="testDate1.txt";
    private String inputFileAll="testDate1.txt;testDate2.txt;";

    /**
     * 构建job
     */
    @Bean(name = "fileJob")
    public Job importFileJob() throws IOException {
        return jobs.get("importFileJob")
                .incrementer(new RunIdIncrementer())//添加一个自增的值，以区分不同的Job实例，当然，这个值在job的其他的地方并不会用到，仅仅是为了标示不同JobInstance
                .listener(getExeCompletionListener())
                .start(step1())
                .build();
    }

    /**
     * 构建job（读取多个文件）
     */
    @Bean(name = "filesJob")
    public Job importFileJob2() throws IOException {
        return jobs.get("importFileJob2")
                .incrementer(new RunIdIncrementer())
                .listener(getExeCompletionListener())
                .start(step2())
                .build();
    }

    /**
     * 声明step(读取单个文件)
     */
    @Bean(name = "importFileJob-step1")
    public Step step1() throws IOException {
        logger.error("importFileJob-step1");
        return stepBuilderFactory.get("importFileJob-step1")
                .<BlackListDO, BlackListDO>chunk(10)//每10个记录提交一次事物（在写入数据库时候可以看出区别（串行执行可以看出））
                .reader(reader())
                .processor(processor())
                .writer(writer())
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
     * 声明step(读取多个文件)
     */
    @Bean(name = "importFileJob-step2")
    public Step step2() throws IOException {
        logger.error("importFileJob-step2");
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(1);
        return stepBuilderFactory.get("importFileJob-step2")
                .<BlackListDO, BlackListDO>chunk(1)//每一条记录提交一次事物
                .reader(readerMore())
                .processor(processor())
                .listener(logProcessListener())
                .writer(writer())
                .faultTolerant()
                .retry(Exception.class)   // 重试
                .noRetry(ParseException.class)
                .retryLimit(1)           //每条记录重试一次
                .listener(new RetryFailuireItemListener())
                .skip(Exception.class)
                .skipLimit(10)         //一共允许跳过200次异常
                .taskExecutor(taskExecutor) //设置并发方式执行
                .throttleLimit(10)        //并发任务数为 10,默认为4
                .transactionManager(transactionManager)
                .build();
    }

    /**
     * 读取单个外部文件方法
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "importFileJob-step1-read")
    @StepScope
    public ItemReader<BlackListDO> reader() throws IOException {
        logger.info("inputFile:" + new ClassPathResource(inputFile).getURL().getPath());
        if (inputFile == null) {
            logger.error("The blacklist reader file is null");
            return null;
        }
        FlatFileItemReader<BlackListDO> reader = new FlatFileItemReader<BlackListDO>();
        reader.setResource(new ClassPathResource(inputFile));
        reader.open(new ExecutionContext());
        reader.setLineMapper(lineMapper());
        reader.setLinesToSkip(1);

        return reader;
    }

    /**
     * 读取多个外部文件方法
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "importFileJob-step1-readMore")
    @StepScope
    public ItemReader<BlackListDO> readerMore() throws IOException {
        logger.info("filepath:inputFile");
        if (StrUtil.isEmpty(inputFileAll)) {
            logger.error("The blacklist reader file is null");
            return null;
        }
        String[] inputFiles = inputFileAll.split(";");
        Resource[] resources = new Resource[inputFiles.length];
        for (int i=0;i<inputFiles.length;i++) {
            logger.info("inputFile:" + new ClassPathResource(inputFiles[i]).getURL().getPath());
            Resource resource = new ClassPathResource(inputFiles[i]);
            resources[i]=resource;
        }
        MultiResourceItemReader reader = new MultiResourceItemReader();
        reader.setResources(resources);

        FlatFileItemReader<BlackListDO> flatReader = new FlatFileItemReader<BlackListDO>();
        flatReader.setLineMapper(lineMapper());
        flatReader.setLinesToSkip(1);

        reader.setDelegate(flatReader);
        reader.open(new ExecutionContext());

        return reader;
    }

    /**
     * 读取文本行映射POJO
     * @return
     */
    @Bean
    @StepScope
    public LineMapper<BlackListDO> lineMapper() {
        DefaultLineMapper<BlackListDO> lineMapper = new DefaultLineMapper<BlackListDO>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"type", "value", "deleteFlag"});

        BeanWrapperFieldSetMapper<BlackListDO> fieldSetMapper = new BeanWrapperFieldSetMapper<BlackListDO>();
        fieldSetMapper.setTargetType(BlackListDO.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new BlackListFieldSetMapper());
        return lineMapper;
    }

    /**
     * 处理过程
     * @return
     */
    @Bean(name = "importFileJob-step1-processor")
    @StepScope
    public ItemProcessor<BlackListDO, BlackListDO> processor() {
        return new BlackListDOItemProcessor(inputFile);
    }

    /**
     * 写出内容
     * @return
     */
    @Bean(name = "importFileJob-step1-writer")
    @StepScope
    public ItemWriter<BlackListDO> writer() {
        return new BlackListItemWriter();
    }

    @Bean(name = "jobExeListener")
    public JobCompletionNotificationListener getExeCompletionListener(){
        return new JobCompletionNotificationListener();
    }

    @Bean
    public LogProcessListener logProcessListener() {
        return new LogProcessListener();
    }
}  