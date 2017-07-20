package com.wwj.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceConfiguration extends ResourceServerConfigurerAdapter {

    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/home").permitAll()//访问：无需登录认证权限
//                .antMatchers("/test/restTemplateRes").permitAll()
                .anyRequest().authenticated() //其他所有资源都需要认证，登陆后访问
                .antMatchers("/account/**").hasAuthority("ADMIN") //登陆后之后拥有“ADMIN”权限才可以访问/hello方法，否则系统会出现“403”权限不足的提示
                .and()
                .formLogin()
                .loginPage("/login")//指定登录页是”/login”
                .permitAll()
//                .successHandler(loginSuccessHandler()) //登录成功后可使用loginSuccessHandler()存储用户信息，可选。
                .and()
                .logout()
                .permitAll()
                .invalidateHttpSession(true);
    }

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(
            @Qualifier("loadBalancedRestTemplate") RestTemplate keyUriRestTemplate) {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getKeyFromAuthorizationServer(keyUriRestTemplate));
        return converter;
    }

    private String getKeyFromAuthorizationServer(RestTemplate keyUriRestTemplate) {
        // Load available UAA servers
        discoveryClient.getServices();
        HttpEntity<Void> request = new HttpEntity<Void>(new HttpHeaders());
        Object obj = keyUriRestTemplate
                .postForObject("http://oauth2-server/oauth/token_key",null,Object.class);
        return (String) keyUriRestTemplate
                .exchange("http://oauth2-server/oauth/token_key", HttpMethod.GET, request, Map.class).getBody()
                .get("value");
    }

}
