package com.wwj.config;

/**
 * 默认的token是放在内存中的，生产环境一般是放在数据库中的
 */
//@Configuration
//@EnableAuthorizationServer
//public class Oauth2Config extends AuthorizationServerConfigurerAdapter {
//    @Resource(name = "authenticationManagerBean")
//    private AuthenticationManager authenticationManager;
//
//    @Resource
//    private DataSource dataSource;
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        /*
//        For a better client design, this should be done by a ClientDetailsService (similar to UserDetailsService).
//         */
//        clients.inMemory() // 使用in-memory存储
//                .withClient("client") // client_id
//                .secret("secret") // client_secret
//                .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code") // 该client允许的授权类型
//                .scopes("app"); // 允许的授权范围
//    }
//
//    //   token保存在数据库的配置：http://blog.csdn.net/neosmith/article/details/52539927
//    @Bean // 声明TokenStore实现
//    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }
//
//    @Bean // 声明 ClientDetails实现
//    public ClientDetailsService clientDetails() {
//        return new JdbcClientDetailsService(dataSource);
//    }
//
//    @Override // 配置框架应用上述实现
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager);
//        endpoints.tokenStore(tokenStore());
//
//        // 配置TokenServices参数
//        endpoints.tokenServices(initTokenService(endpoints));
//    }
//
//    @Primary
//    @Bean
//    public DefaultTokenServices initTokenService(AuthorizationServerEndpointsConfigurer endpoints){
//        // 配置TokenServices参数
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(endpoints.getTokenStore());
//        tokenServices.setSupportRefreshToken(false);
//        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
//        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
//        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
//        return tokenServices;
//    }

//}
