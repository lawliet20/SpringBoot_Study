package com.wwj.controller;

import com.wwj.model.User;
import com.wwj.service.HelloService;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sherry on 2017/4/16.
 */
@RestController
public class RemoteController {
    @Resource
    private HelloService helloService;

    /*提供接口给其他微服务调用（此接口无权限控制）*/
    @RequestMapping("/test/restTemplateRes")
    public ResponseEntity<List<User>> testRestTemplateRes(){
        System.out.println("springBoot_service->testRestTemplateRes");
        List<User> list = helloService.responseAllUser();
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }

    /*测试访问远程微服务接口（远程接口有权限控制）*/
    @RequestMapping("/test/restTemplateReq")
    public void testRestTemplateReq(){
        List<User> list = helloService.requestAllUser();
        System.out.println("###:"+JSONUtil.toJsonStr(list));
    }


}
