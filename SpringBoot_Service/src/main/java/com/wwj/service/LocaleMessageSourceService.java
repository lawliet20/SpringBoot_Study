package com.wwj.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

@Component
public class LocaleMessageSourceService {

    @Resource
    private MessageSource messageSource;

    /**
     * @param code ：对应messages配置的key.
     * @return
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    public String getMessage(String code, Object[] objects) {
        Locale locale = LocaleContextHolder.getLocale();
//        Locale locale1= RequestContextUtils.getLocale(request);
        return messageSource.getMessage(code, objects, locale);
    }
}