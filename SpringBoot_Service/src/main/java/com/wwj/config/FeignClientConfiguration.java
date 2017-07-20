//package com.wwj.config;
//
//import feign.RequestInterceptor;
//import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
//import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 通过FeignClient并自带Token验证功能
// * Created by zhangjinye on 2017/5/20.
// */
//@Configuration
//public class FeignClientConfiguration {
//
//    //授权类型
//    private String grantType = "password";
//
//    //accessTokenUri
//    private String accessTokenUri="http://localhost:8071/uaa/oauth/token";
//
//    //clientId
//    private String clientId="client";
//
//    //clientSecret
//    private String clientSecret="secret";
//
//    //scope
//    private String scope="app";
//
//    @Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor() {
//        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), xmOauth2RemoteResource());
//    }
//
//
//    public ClientCredentialsResourceDetails xmOauth2RemoteResource() {
//        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
//        resourceDetails.setGrantType(grantType);
//        resourceDetails.setAccessTokenUri(accessTokenUri);
//        resourceDetails.setClientId(clientId);
//        resourceDetails.setClientSecret(clientSecret);
//        List<String> list = new ArrayList<String>();
//        list.add(scope);
//        resourceDetails.setScope(list);
//        return resourceDetails;
//    }
//
//}
