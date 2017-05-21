package com.wwj.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sherry on 2017/5/18.
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("hello");
        return "hello";
    }
}
