package com.wwj.config.batchJob.processor;

import com.wwj.config.batchJob.domain.Message;
import com.wwj.config.batchJob.mapper.BlackListFieldSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class MessageItemProcessor implements ItemProcessor<Message, Message> {
    private static final Logger log = LoggerFactory.getLogger(BlackListFieldSetMapper.class);

    // 数据处理
    public Message process(Message message) throws Exception {
        String threadName = Thread.currentThread().getName();
        log.info("线程{}->开始数据处理...",threadName);
        message.setId(UUID.randomUUID().toString());
        log.info("线程{}->处理中数据message：{}",threadName, message.toString());
        return message;
    }

} 