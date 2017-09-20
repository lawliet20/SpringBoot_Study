package com.wwj.common.error;

import java.io.Serializable;
import java.util.Map;

public class ParameterizedErrorVM implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String message;
    private final Map<String, String> paramMap;

    public ParameterizedErrorVM(String message, Map<String, String> paramMap) {
        this.message = message;
        this.paramMap = paramMap;
    }

    public String getMessage() {
        return this.message;
    }

    public Map<String, String> getParams() {
        return this.paramMap;
    }
}
