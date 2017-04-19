package com.wwj.model;

import java.io.Serializable;

/**
 * Created by sherry on 2017/4/16.
 */
public class User implements Serializable{

    public User(){

    }

    public User(String userName){
        this.userName = userName;
    }

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
