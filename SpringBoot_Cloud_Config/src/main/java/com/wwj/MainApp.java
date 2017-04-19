package com.wwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 当前项目功能：
 * 参考链接：http://www.cnblogs.com/skyblog/p/5127690.html
 * 分布式配置管理、
 * Eureka服务器、
 *
 * Created by sherry on 2017/4/16.
 */
@SpringBootApplication
@EnableConfigServer//开启cloud配置功能
@EnableEurekaServer//开启cloud注册服务与发现
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}
