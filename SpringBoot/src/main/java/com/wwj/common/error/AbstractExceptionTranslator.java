package com.wwj.common.error;

import com.wwj.common.exception.CustomParameterizedException;
import com.wwj.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public abstract class AbstractExceptionTranslator {
    private final Logger log = LoggerFactory.getLogger(AbstractExceptionTranslator.class);

    private final MessageSource messageSource;

    protected AbstractExceptionTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorVM processConcurrencyError(ConcurrencyFailureException ex) {
        ErrorVM error = new ErrorVM(ErrorConstants.ERR_CONCURRENCY_FAILURE, localTranslate(ErrorConstants.ERR_CONCURRENCY_FAILURE));
        error.setCause(ex.getMessage());
        log.error("ConcurrencyFailureException[" + error.getErrorId() + "] : " + ex.getMessage(), ex.getMessage());
        log.error("Cause : ", ex);
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorVM error = new ErrorVM(ErrorConstants.ERR_VALIDATION, localTranslate(ErrorConstants.ERR_VALIDATION));
        for (FieldError fieldError : fieldErrors) {
            FieldErrorVM field = new FieldErrorVM(
                    fieldError.getObjectName(),
                    fieldError.getField(),
                    fieldError.getCode(),
                    fieldError.getArguments(),
                    fieldError.getDefaultMessage()
            );
            error.add(field);
        }
        log.error("MethodArgumentNotValidException[" + error.getErrorId() + "] : " + ex.getMessage(), ex);
        return error;
    }

    @ExceptionHandler(CustomParameterizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processParameterizedValidationError(CustomParameterizedException ex) {
        ErrorVM error = new ErrorVM(ex.getMessage());
        log.error("CustomParameterizedException[" + error.getErrorId() + "] : " + ex.getMessage(), ex.getMessage());
        return error;
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processServiceError(ServiceException ex) {
        ErrorVM error = new ErrorVM(ex.getErrorCode(), localTranslate(ex.getErrorCode()));
        log.error("ServiceException[" + error.getErrorId() + "]: " + ex.getMessage(), error.getMessage());
        return error;
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ResponseBody
//    public ErrorVM processAccessDeniedException(AccessDeniedException ex) {
//        ErrorVM error = new ErrorVM(ErrorConstants.ERR_ACCESS_DENIED, localTranslate(ErrorConstants.ERR_ACCESS_DENIED));
//        error.setCause(ex.getMessage());
//        log.error("AccessDeniedException[" + error.getErrorId() + "]: " + ex.getMessage(), error.getMessage());
//        return error;
//    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorVM processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorVM error = new ErrorVM(ErrorConstants.ERR_METHOD_NOT_SUPPORTED, ex.getMessage());
        log.error("HttpRequestMethodNotSupportedException[" + error.getErrorId() + "]: " + ex.getMessage(), error.getMessage());
        return error;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVM> processException(Exception ex) {
        ResponseEntity.BodyBuilder builder;
        ErrorVM error;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            error = new ErrorVM("error." + responseStatus.value().value(), responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            error = new ErrorVM(ErrorConstants.ERR_INTERNAL_SERVER_ERROR, localTranslate(ErrorConstants.ERR_INTERNAL_SERVER_ERROR));
            error.setCause(ex.getMessage());
        }
        log.error("Exception[" + error.getErrorId() + "]: " + ex.getMessage(), ex);
        return builder.body(error);
    }

    private String localTranslate(String errorCode, String... params) {
        return messageSource.getMessage(errorCode, params, LocaleContextHolder.getLocale());
    }
}
