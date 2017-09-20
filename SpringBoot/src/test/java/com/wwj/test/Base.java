package com.wwj.test;

/**
 * Created by sherry on 2017/9/6.
 */
public class Base {
    private String name = this.getClass().getSimpleName();

    public String who(){
        return this.getClass().getName();
    }

    public void showMe(){
        System.out.println(who());
        System.out.println(name);
    }
}
