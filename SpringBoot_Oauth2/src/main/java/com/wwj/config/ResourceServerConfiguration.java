package com.wwj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

//        private final TokenStore tokenStore;
//
//        private final JHipsterProperties jHipsterProperties;
//
//        private final CorsFilter corsFilter;
//
//        public ResourceServerConfiguration(TokenStore tokenStore, JHipsterProperties jHipsterProperties, CorsFilter corsFilter) {
//            this.tokenStore = tokenStore;
//            this.jHipsterProperties = jHipsterProperties;
//            this.corsFilter = corsFilter;
//        }

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
//                .antMatchers("/hello/**").authenticated()
                .antMatchers("/oauth/**").permitAll();
    }

//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.resourceId("jhipster-uaa").tokenStore(tokenStore);
//    }
}