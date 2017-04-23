package com.wwj.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.wwj.model.User;
import com.wwj.service.DaoTestService;
import com.wwj.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

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
    private HelloService helloService;
    @Resource
    private DaoTestService daoTestService;

    @RequestMapping("/")
    public String index() {
        List<User> list = helloService.searchAll();
        System.out.println(JSONUtils.toJSONString("####:"+list));
        return "index";
    }

    @RequestMapping("/hello")
    public String hello() throws Exception {
        daoTestService.testSelect();
        return "hello";
    }

    @RequestMapping("/transaction")
    public String transaction() throws RuntimeException {
        daoTestService.testTransaction();
        return "hello";
    }

    @RequestMapping("/exception")
    public String exception() throws Exception {
        throw new Exception();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

}