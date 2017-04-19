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
@EnableEurekaServer
public class EurekaMainApp {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMainApp.class, args);
        String errorString="emergency! eureka may be incorrectly claiming instances are up when they're not. renewals are lesser than threshold and hence the instances are not being expired just to be safe.";
        errorString="the self preservation mode is turned off.this may not protect instance expiry in case of network/other problems.";
    }
}
