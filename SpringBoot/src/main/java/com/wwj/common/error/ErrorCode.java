package com.wwj.common.error;

public enum ErrorCode {
    ERROR_TIMEOUT("TIMEOUT", "Service Timeout"), ERROR_UNKNOWN("UNKNOWN", "Unknow error"), ERROR_ID_EXISTS("ID_EXISTS", "ID existed");

    private String code;
    private String message;

    private ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
