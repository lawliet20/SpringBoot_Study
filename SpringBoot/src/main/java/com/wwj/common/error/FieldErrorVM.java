package com.wwj.common.error;

import java.io.Serializable;

public class FieldErrorVM implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String objectName;
    private final String field;
    private final String message;
    private final String defaultMessage;
    private final Object[] arguments;

    public FieldErrorVM(String dto, String field, String message, Object[] arguments) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
        this.arguments = arguments;
        this.defaultMessage = "";
    }

    public FieldErrorVM(String dto, String field, String message, Object[] arguments, String defaultMessage) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public String getField() {
        return this.field;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDefaultMessage() {
        return this.defaultMessage;
    }

    public Object[] getArguments() {
        return this.arguments;
    }
}
