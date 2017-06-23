package com.wwj.config.batchJob.mapper;

import com.wwj.config.batchJob.domain.BlackListDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Created by sherry on 2017/6/6.
 */
public class BlackListFieldSetMapper implements FieldSetMapper<BlackListDO> {
    private static final Logger log = LoggerFactory.getLogger(BlackListFieldSetMapper.class);

    @Override
    public BlackListDO mapFieldSet(FieldSet fieldSet) throws BindException {
        String threadName = Thread.currentThread().getName();
        log.info("线程{}->开始读取数据后的对象转换...",threadName);
        BlackListDO blackListDO = new BlackListDO();
        blackListDO.setDeleteFlag(fieldSet.readInt("deleteFlag"));
        blackListDO.setType(fieldSet.readString("type"));
        blackListDO.setValue(fieldSet.readString("value"));
        log.info("线程{}->转换后对象BlackListDO：{}",threadName,blackListDO.toString());
        return blackListDO;
    }


}
