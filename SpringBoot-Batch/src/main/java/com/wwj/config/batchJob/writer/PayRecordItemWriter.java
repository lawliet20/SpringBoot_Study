package com.wwj.config.batchJob.writer;

import com.wwj.config.batchJob.domain.PayRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class PayRecordItemWriter implements ItemWriter<PayRecord> {
    private static final Logger log = LoggerFactory.getLogger(PayRecordItemWriter.class);

    @Override
    public void write(List<? extends PayRecord> payRecord) throws Exception {
        // 插入数据库操作
        String threadName = Thread.currentThread().getName();
        log.info("线程{}->写入数据库：{}",threadName,payRecord);
    }

}  