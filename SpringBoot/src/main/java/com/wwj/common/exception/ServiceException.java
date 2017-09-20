package com.wwj.common.exception;

import com.wwj.common.error.ErrorCode;

public class ServiceException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public ServiceException(String errorCode) {
        this(errorCode, "");
    }

    public ServiceException(ErrorCode code) {
        this(code.getCode(), code.getMessage());
    }

    public ServiceException(ErrorCode code, Throwable cause) {
        this(code.getCode(), code.getMessage(), cause);
    }

    public ServiceException(String errorCode, String errorMessage) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ServiceException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = "";
    }

    public ServiceException(String errorCode, String errorMessage, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
