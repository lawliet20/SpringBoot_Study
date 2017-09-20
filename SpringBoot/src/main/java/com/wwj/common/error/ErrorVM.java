package com.wwj.common.error;


import com.xiaoleilu.hutool.util.RandomUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorVM implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String errorId;
    private final String message;           // 错误消息
    private final String description;       // 错误描述
    private String cause;                   // 错误原因，未知异常需要记录出错原因
    private String param;
    private String entityName;              // 错误实体
    private List<FieldErrorVM> fieldErrors; // 错误字段

    public ErrorVM(String message) {
        this(message, "");
    }

    public ErrorVM(String message, String description) {
        this(message, description, "", null);
    }

    public ErrorVM(String message, String description, List<FieldErrorVM> fieldErrors) {
        this(message, description, "", fieldErrors);
    }

    public ErrorVM(String message, String description, String entityName, List<FieldErrorVM> fieldErrors) {
        this.errorId = RandomUtil.randomNumbers(8);
        this.message = message;
        this.entityName = entityName;
        this.description = description;
        this.fieldErrors = fieldErrors;
    }

    public void add(FieldErrorVM fieldError) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(fieldError);
    }

    public String getErrorId() {
        return errorId;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public List<FieldErrorVM> getFieldErrors() {
        return fieldErrors;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
