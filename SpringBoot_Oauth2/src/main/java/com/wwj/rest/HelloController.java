package com.wwj.rest;

import com.wwj.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sherry on 2017/5/18.
 */
@RestController
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("hello");
        return "hello";
    }

    @RequestMapping("/hello/say")
    public void say(){
        System.out.println("say");
    }

    @RequestMapping("/hello2")
    public ResponseEntity<OAuth2AccessToken> getResponse() {
        OAuth2AccessToken accessToken = new DefaultOAuth2AccessToken("abc");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        return new ResponseEntity<OAuth2AccessToken>(accessToken, headers, HttpStatus.OK);
    }

    @RequestMapping("/getCurrentUser")
    public String getCurrentUser() {
        return UserDetailsServiceImpl.getCurrentUser();
    }

    @RequestMapping("/secure")
    public String doSecure() {
        logger.info("do secure...");
        return "secure";
    }

}
