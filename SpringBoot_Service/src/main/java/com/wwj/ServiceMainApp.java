package com.wwj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 当前项目功能：
 * 参考链接：http://www.cnblogs.com/skyblog/p/5133752.html
 * Eureka服务器
 * Created by sherry on 2017/4/16.
 */
@SpringBootApplication
@EnableDiscoveryClient
//To make your web-app into an OAuth2 client
//@EnableOAuth2Client
public class ServiceMainApp {
    private static final Logger log = LoggerFactory.getLogger(ServiceMainApp.class);

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(ServiceMainApp.class);
        Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getActiveProfiles());

        String configServerStatus = env.getProperty("configserver.status");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus == null ? "Not found or not setup for this application" : configServerStatus);

    }
}
