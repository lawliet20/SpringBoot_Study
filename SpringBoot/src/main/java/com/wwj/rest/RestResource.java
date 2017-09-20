package com.wwj.rest;

import com.wwj.common.rest.BaseResource;
import com.wwj.domain.User;
import com.wwj.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 标准rest返回
 * Created by sherry on 2017/9/6.
 */
@RequestMapping("/api/user")
public class RestResource extends BaseResource{
    @Resource
    private UserService userService;

    @GetMapping
    public ResponseEntity<User> getUser(){
        return listResponse(userService.getAllUser());
    }
}
