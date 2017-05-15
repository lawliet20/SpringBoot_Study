package com.wwj.controller;

import com.wwj.service.DaoTestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 *
 * @author 程序猿DD
 * @version 1.0.0
 * @blog http://blog.didispace.com
 *
 */
@Controller
public class HelloController {
    @Resource
    private DaoTestService daoTestService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /*调用数据库*/
    @RequestMapping("/test/mybatis")
    public String hello() throws Exception {
        daoTestService.testSelect();
        return "hello";
    }

    /*测试事物*/
    @RequestMapping("/test/transaction")
    public String transaction() throws RuntimeException {
        daoTestService.testTransaction();
        return "hello";
    }

    /*测试全局异常捕获*/
    @RequestMapping("/test/exception")
    public String exception() throws Exception {
        throw new Exception();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

}