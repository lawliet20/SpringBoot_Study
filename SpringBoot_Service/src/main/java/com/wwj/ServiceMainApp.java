package com.wwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 当前项目功能：
 * 参考链接：http://www.cnblogs.com/skyblog/p/5133752.html
 * Eureka服务器
 * Created by sherry on 2017/4/16.
 */
@SpringBootApplication
@EnableEurekaServer//配置本应用将使用服务注册和服务发现，注意：注册和发现用这一个注解。
public class ServiceMainApp {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMainApp.class, args);
    }
}
