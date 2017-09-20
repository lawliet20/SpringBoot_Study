package com.wwj.rest;

import com.alibaba.fastjson.JSON;
import com.wwj.common.rest.BaseResource;
import com.wwj.model.User;
import com.wwj.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 简单测试接口
 */
@RestController
@RequestMapping("/hello")
public class HelloResource extends BaseResource {
    private static final Logger logger = LoggerFactory.getLogger(HelloResource.class);
    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private UserService userService;

    @PostMapping
    public void hello(User user){
        logger.debug("user info :{}", JSON.toJSONString(user));
    }

    /*调用数据库*/
    @GetMapping("/test/mybatis")
    public String hello() throws Exception {
        userService.getAllUser();
        return "hello";
    }

    /*测试事物*/
    @GetMapping("/test/transaction")
    public String transaction() throws RuntimeException {
        userService.delWithException();
        return "hello";
    }

    /*测试全局异常捕获*/
    @GetMapping("/test/exception")
    public String exception() throws Exception {
        throw new Exception("全局异常测试");
    }

    /*获取配置文件属性*/
    @GetMapping(value = "/getValue")
    public String getValue(){
        return applicationName;
    }


}