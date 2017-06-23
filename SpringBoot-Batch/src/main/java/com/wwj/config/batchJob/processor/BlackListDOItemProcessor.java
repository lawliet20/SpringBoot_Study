package com.wwj.config.batchJob.processor;

import com.wwj.config.batchJob.mapper.BlackListFieldSetMapper;
import com.wwj.config.batchJob.domain.BlackListDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class BlackListDOItemProcessor implements ItemProcessor<BlackListDO, BlackListDO> {
    private static final Logger log = LoggerFactory.getLogger(BlackListFieldSetMapper.class);

    public String inputFile;

    public BlackListDOItemProcessor() {
    }

    public BlackListDOItemProcessor(String inputFile) {
        this.inputFile = inputFile;
    }

    // 数据处理  
    public BlackListDO process(BlackListDO blackListDO) throws Exception {
        String threadName = Thread.currentThread().getName();
        log.info("线程{}->开始数据处理...",threadName);
        blackListDO.setDeleteFlag(0);
        blackListDO.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        log.info("线程{}->处理中数据BlackListDO：{}",threadName, blackListDO.toString());
        return blackListDO;
    }

} 