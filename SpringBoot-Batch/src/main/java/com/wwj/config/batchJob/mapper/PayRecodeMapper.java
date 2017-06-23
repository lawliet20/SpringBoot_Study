package com.wwj.config.batchJob.mapper;

import com.wwj.config.batchJob.domain.Bill;
import com.wwj.config.batchJob.domain.PayRecord;
import com.wwj.config.batchJob.domain.User;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/*
* 扣费mapper
* */
public class PayRecodeMapper implements FieldSetMapper<PayRecord> {
    private static final Logger log = LoggerFactory.getLogger(PayRecodeMapper.class);

    @Override
    public PayRecord mapFieldSet(FieldSet fieldSet) {
        PayRecord payRecord = new PayRecord();
        Bill bill = new Bill();
        User user = new User();

        user.setName(fieldSet.readString("name"));

        bill.setFees(fieldSet.readDouble("fee"));
        bill.setPaidFees(fieldSet.readDouble("paidFees"));
        bill.setUnpaidFees(fieldSet.readDouble("unpaidFees"));
        bill.setPayStatus(fieldSet.readInt("payStatus"));
        bill.setUser(user);

        payRecord.setBill(bill);
        payRecord.setPaidFees(fieldSet.readDouble("paidFees"));
        log.info("payRecord数据读入转型:{}", JSONUtil.toJsonPrettyStr(user));
        return payRecord;
    }

}
