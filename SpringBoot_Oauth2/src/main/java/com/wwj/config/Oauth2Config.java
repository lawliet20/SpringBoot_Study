package com.wwj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * 参考链接：http://blog.csdn.net/j754379117/article/details/70175198
 * 默认的token是放在内存中的，生产环境一般是放在数据库中的
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {
    @Resource(name = "authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Resource
    private DataSource dataSource;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*
        For a better client design, this should be done by a ClientDetailsService (similar to UserDetailsService).
         */
        clients.inMemory() // 使用in-memory存储
                .withClient("client") // client_id
                .secret("secret") // client_secret
                .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code") // 该client允许的授权类型
                .scopes("app"); // 允许的授权范围
        //从数据库读取配置信息
        clients.withClientDetails(clientDetails());
    }

    //   token保存在数据库的配置：http://blog.csdn.net/neosmith/article/details/52539927
    @Bean // 声明TokenStore实现
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean // 声明 ClientDetails实现
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /*声明安全约束，哪些允许访问，哪些不允许访问*/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    /*声明授权和token的端点及token的服务一些配置信息，比如采用声明方式存储、token有效期等*/
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        直接注入一个AuthenticationManager，自动开启密码授权类型
        endpoints.authenticationManager(authenticationManager);
        endpoints.tokenStore(tokenStore());

        // 配置TokenServices参数
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(false);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); // 30天token失效
        endpoints.tokenServices(tokenServices);
    }


//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager).accessTokenConverter(
//                jwtAccessTokenConverter());
//    }

    /**
     * Apply the token converter (and enhander) for token store.
     */
//    @Bean
//    public JwtTokenStore tokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }

    /**
     * This bean generates an token enhancer, which manages the exchange between JWT acces tokens and Authentication
     * in both directions.
     *
     * @return an access token converter configured with the authorization server's public/private keys
     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        KeyPair keyPair = new KeyStoreKeyFactory(
//                new ClassPathResource("keystore.jks"), "password".toCharArray())
//                .getKeyPair("selfsigned");
//        converter.setKeyPair(keyPair);
//        return converter;
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess(
//                "isAuthenticated()");
//    }
}
