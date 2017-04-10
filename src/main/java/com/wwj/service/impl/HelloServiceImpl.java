package com.wwj.service.impl;

import com.wwj.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * Created by sherry on 2017/3/19.
 */
@Service("helloService")
public class HelloServiceImpl implements HelloService {

    @Override
    public String hi() {
        return "hello";
    }
}
