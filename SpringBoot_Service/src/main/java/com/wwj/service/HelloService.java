package com.wwj.service;

import com.wwj.model.User;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 2017/4/16.
 */
@Service
public class HelloService {
    @Resource(name = "restTemplate")
    RestTemplate restTemplate;

    final String SERVICE_NAME = "spring-boot";

    public List<User> responseAllUser(){
        List<User> list = new ArrayList<User>();
        User user1 = new User("wwj");
        User user2 = new User("sherry");
        User user3 = new User("qw");
        list.add(user1);
        list.add(user2);
        list.add(user3);
        return list;
    }

    @LoadBalanced
    public List<User> requestAllUser(){
        return restTemplate.getForObject("http://" + SERVICE_NAME + "/test/restTemplateRes", List.class);
    }
}
