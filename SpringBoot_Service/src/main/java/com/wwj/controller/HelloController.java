package com.wwj.controller;

import com.wwj.model.User;
import com.wwj.service.HelloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sherry on 2017/4/16.
 */
@Controller
public class HelloController {
    @Resource
    private HelloService helloService;

    @RequestMapping("/hello")
    public ResponseEntity<List<User>> hello(){
        System.out.println("springBoot_service->hello");
        List<User> list = helloService.hello();
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }
}
