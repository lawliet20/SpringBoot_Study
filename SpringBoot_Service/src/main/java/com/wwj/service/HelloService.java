package com.wwj.service;

import com.wwj.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 2017/4/16.
 */
@Service
public class HelloService {

    public List<User> hello(){
        List<User> list = new ArrayList<User>();
        User user1 = new User("wwj");
        User user2 = new User("sherry");
        User user3 = new User("qw");
        list.add(user1);
        list.add(user2);
        list.add(user3);
        return list;
    }
}
