package com.wwj.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wwj.model.User;
import com.wwj.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 2017/3/19.
 */
@Service("helloService")
public class RemoteServiceImpl implements RemoteService {

    @Autowired
    RestTemplate restTemplate;

    final String SERVICE_NAME = "simple-service";

    @Override
    public String hi() {
        return "hello";
    }

    /**
     * 通过restTemplate可以调用在eureka注册的服务
     * HystrixCommand断路器表示当eureka服务调用失败时，调用fallbackSearchAll服务
     */
    @HystrixCommand(fallbackMethod = "fallbackSearchAll")
    public List<User> requestUserList() {
        return restTemplate.getForObject("http://" + SERVICE_NAME + "/test/restTemplateRes", List.class);
    }

    private List<User> fallbackSearchAll() {
        System.out.println("HystrixCommand fallbackMethod handle!");
        List<User> ls = new ArrayList<User>();
        User user = new User();
        user.setUserName("TestHystrix");
        ls.add(user);
        return ls;
    }

    @Override
    public List<User> responseUserList() {
        List<User> list = new ArrayList<User>();
        User user1 = new User("aaa");
        User user2 = new User("bbb");
        User user3 = new User("ccc");
        list.add(user1);
        list.add(user2);
        list.add(user3);
        return list;
    }
}
