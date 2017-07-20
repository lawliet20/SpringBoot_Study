//package com.wwj.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
//import javax.annotation.Resource;
//
///**
// * jwt实现的权限校验
// */
//@Configuration
//@EnableAuthorizationServer
//public class Oauth2Config extends AuthorizationServerConfigurerAdapter {
//    @Resource(name = "authenticationManagerBean")
//    private AuthenticationManager authenticationManager;
//
//    @Resource
//    private JwtTokenStore tokenStore;
//
//    @EnableResourceServer
//    public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            http
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/oauth/token_key").permitAll()
//                    .antMatchers("/hello").permitAll();
//        }
//
//        @Override
//        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//            resources.resourceId("oauth2-server").tokenStore(tokenStore);
//        }
//
//    }
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory() // 使用in-memory存储
//                .withClient("client") // client_id
//                .secret("secret") // client_secret
//                .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code") // 该client允许的授权类型
//                .scopes("app"); // 允许的授权范围
//    }
//
//    @Bean
//    public JwtTokenStore tokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    /**
//     * This bean generates an token enhancer, which manages the exchange
//     * between JWT acces tokens and Authentication in both directions.
//     * @return an access token converter configured with the authorization server's public/private keys
//     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey("123");
//        return converter;
//    }
//
//    /*声明授权和token的端点及token的服务一些配置信息，比如采用声明方式存储、token有效期等*/
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        //默认情况下所有授权类型都支持，除了密码授权类型。 直接注入一个AuthenticationManager，自动开启密码授权类型
//        endpoints.authenticationManager(authenticationManager)
//                .accessTokenConverter(jwtAccessTokenConverter())
//                .tokenStore(tokenStore);
//
///*  // 配置TokenServices参数
//        endpoints.tokenServices(initTokenService(endpoints));
//        //默认的/oauth/token接口将被/hello2接口所取代。默认接口直接403不可访问
//        endpoints.pathMapping("/oauth/token","/hello2");
//        //自定义bean可以实现UserDetailsService，作为用户数据的来源。
//        // (只要项目中有UserDetailsService实现类，则自定义实现类会自动生效。此配置可以省略。)
//        endpoints.userDetailsService(userDetailsService);
//      */
//
//    }
//
////    @Primary
////    @Bean
////    public DefaultTokenServices initTokenService(AuthorizationServerEndpointsConfigurer endpoints) {
////        // 配置TokenServices参数
////        DefaultTokenServices tokenServices = new DefaultTokenServices();
////        tokenServices.setTokenStore(endpoints.getTokenStore());
////        tokenServices.setSupportRefreshToken(false);
////        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
////        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
////        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
////        return tokenServices;
////    }
//
///*声明安全约束，哪些允许访问，哪些不允许访问*/
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        super.configure(security);
//        security.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("isAuthenticated()");
//    }
//
//
//}
