package com.wwj.config.batchJob.mapper;

import com.wwj.config.batchJob.domain.Message;
import com.wwj.config.batchJob.domain.User;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/*
* 扣费通知mapper
* */
public class MessageMapper implements FieldSetMapper<Message> {
    private static final Logger log = LoggerFactory.getLogger(MessageMapper.class);

    @Override
    public Message mapFieldSet(FieldSet fieldSet) {
        Message message = new Message();
        User user = new User();

        user.setName(fieldSet.readString("name"));

        message.setContent(fieldSet.readRawString("content"));
        message.setUser(user);

        log.info("Message数据读入转型:{}", JSONUtil.toJsonPrettyStr(user));
        return message;
    }

}
