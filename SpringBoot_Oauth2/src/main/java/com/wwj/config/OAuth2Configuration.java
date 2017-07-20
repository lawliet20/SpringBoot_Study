package com.wwj.config;

import com.wwj.security.Authorities;
import com.wwj.security.CustomAuthenticationEntryPoint;
import com.wwj.security.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class OAuth2Configuration {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
//                    .and()
//                    .logout()
//                    .logoutUrl("/oauth/logout")
//                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .and()
                    .csrf()
                    .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                    .disable()
                    .headers()
                    .frameOptions().disable()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    //带/hello/的不需要验证
                    .antMatchers("/hello/**").permitAll()
                    //带/secure/**的需要验证
                    .antMatchers("/secure/**").authenticated();

        }

        //持久化oauth2 token
        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(accessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setSigningKey("1234");
            return converter;
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter implements EnvironmentAware {

        private static final String ENV_OAUTH = "authentication.oauth.";
        private static final String PROP_CLIENTID = "clientid";
        private static final String PROP_SECRET = "secret";
        private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValidityInSeconds";

        private RelaxedPropertyResolver propertyResolver;


        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Bean
        public TokenEnhancer tokenEnhancer() {
            return new CustomTokenEnhancer();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(
                    Arrays.asList(tokenEnhancer(), accessTokenConverter()));
            endpoints
                    .tokenStore(tokenStore())
                    .tokenEnhancer(tokenEnhancerChain)
                    .authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()
                    .withClient("web_social_app")
                    .secret("web_social_app_secret")
                    .scopes("read", "write")
                    .authorities(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_USER.name())
                    .authorizedGrantTypes("password", "refresh_token")
                    //设置token有效时间20s
                    .accessTokenValiditySeconds((int)TimeUnit.SECONDS.toSeconds(20));
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);
        }

        //采用JWT来存储token tokenstore
        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(accessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            KeyPair keyPair = new KeyStoreKeyFactory(
                    new ClassPathResource("keystore.jks"), "password".toCharArray())
                    .getKeyPair("selfsigned");
            //设置签名的秘钥
//            converter.setSigningKey("123");
            converter.setKeyPair(keyPair);
            return converter;
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()");
        }
    }

}
