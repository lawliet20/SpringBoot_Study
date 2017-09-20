package com.wwj.rest.errors;

import com.wwj.common.error.AbstractExceptionTranslator;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Created by sherry on 2017/9/5.
 */
@ControllerAdvice
public class ExceptionHandler extends AbstractExceptionTranslator {

    protected ExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }
}
