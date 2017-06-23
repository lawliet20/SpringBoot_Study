package com.wwj.config.batchJob.writer;

import com.wwj.config.batchJob.domain.User;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class UserItemWriter implements ItemWriter<User> {
    private static final Logger log = LoggerFactory.getLogger(UserItemWriter.class);

    @Override
    public void write(List<? extends User> userList) throws Exception {
        // 插入数据库操作
        log.info("user写入数据库...");
        log.info(JSONUtil.toJsonStr(userList));
    }

}  