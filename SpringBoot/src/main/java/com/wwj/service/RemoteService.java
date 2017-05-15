package com.wwj.service;

import com.wwj.model.User;

import java.util.List;

/**
 * Created by sherry on 2017/3/19.
 */
public interface RemoteService {

    public String hi();

    public List<User> requestUserList();

    public List<User> responseUserList();
}
