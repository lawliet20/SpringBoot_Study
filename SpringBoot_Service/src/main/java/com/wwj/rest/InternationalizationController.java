package com.wwj.rest;

import com.wwj.service.LocaleMessageSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by on 2017.03.08.
 */
@RestController
@RequestMapping("/global")
public class InternationalizationController {
    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @RequestMapping(method = RequestMethod.GET,path = "/local")
    public void readFoo(HttpServletRequest request) {
        System.out.println(localeMessageSourceService.getMessage("welcome"));
    }

}
