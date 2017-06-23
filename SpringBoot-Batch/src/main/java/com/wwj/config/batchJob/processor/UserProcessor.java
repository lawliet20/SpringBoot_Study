package com.wwj.config.batchJob.processor;

import com.wwj.config.batchJob.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<User, User> {
    private static final Logger log = LoggerFactory.getLogger(UserProcessor.class);


    // 数据处理  
    public User process(User user) throws Exception {
        log.info("user开始数据处理...");
        user.setEmail("测试email...");
        log.info("user处理中数据：{}", user.toString());
        return user;
    }

} 