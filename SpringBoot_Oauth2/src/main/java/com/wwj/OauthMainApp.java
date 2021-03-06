package com.wwj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by sherry on 2017/3/19.
 */
@ComponentScan
@SpringBootApplication
//@EnableDiscoveryClient//让服务使用eureka服务器，只需添加@EnableDiscoveryClient注解
public class OauthMainApp {

    private static final Logger log = LoggerFactory.getLogger(OauthMainApp.class);

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(OauthMainApp.class);
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

    /*
    * 往ACT_ID_USER表插入数据
    * */
//    @Bean
//    InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {
//        return new InitializingBean() {
//            public void afterPropertiesSet() throws Exception {
//
//                User admin = identityService.newUser("admin");
//                admin.setPassword("admin");
//                identityService.saveUser(admin);
//
//            }
//        };
//    }
}
