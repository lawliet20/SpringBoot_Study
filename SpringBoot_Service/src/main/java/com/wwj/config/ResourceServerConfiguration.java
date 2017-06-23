package com.wwj.config;

import com.xiaoleilu.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                .and()
                .csrf()
                .disable()
//                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/foo").hasAuthority("FOO_READ")
                .antMatchers("/hello/**").hasAuthority("ADMIN")
                .antMatchers("/oauth/**").permitAll();
    }


    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(
            @Qualifier("restTemplate") RestTemplate keyUriRestTemplate) {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setVerifierKey(getKeyFromAuthorizationServer(keyUriRestTemplate));
//        return converter;
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    private String getKeyFromAuthorizationServer(@Qualifier("oAuth2RestTemplate")RestTemplate keyUriRestTemplate) {
        // Load available UAA servers
        discoveryClient.getServices();
        HttpEntity<Void> request = new HttpEntity<Void>(new HttpHeaders());
        Object obj = keyUriRestTemplate.exchange("http://oauth2-server/oauth/token_key", HttpMethod.GET, request, Map.class);
        System.out.println(JSONUtil.toJsonStr(obj));
//      /oauth/token_key：如果jwt模式则可以用此来从认证服务器获取公钥
        String res = (String) keyUriRestTemplate
                .exchange("http://oauth2-server/oauth/token_key", HttpMethod.GET, request, Map.class).getBody()
                .get("value");
        System.out.println("########:"+res);
        return res;
    }

}