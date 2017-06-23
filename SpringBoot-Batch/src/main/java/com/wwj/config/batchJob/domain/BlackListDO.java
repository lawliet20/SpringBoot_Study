package com.wwj.config.batchJob.domain;

import java.io.Serializable;

/**
 * Created by sherry on 2017/6/6.
 */
public class BlackListDO implements Serializable{

    private String uuid;

    private Integer deleteFlag;

    private String type;

    private String value;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BlackListDO{" +
                "uuid='" + uuid + '\'' +
                ", deleteFlag=" + deleteFlag +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
