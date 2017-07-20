//package com.wwj.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    /**
//     * 一般用于设置不需要进行权限过滤的静态资源
//     *
//     * @param web
//     * @throws Exception
//     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/pages/**")
//                .antMatchers("/css/**")
//                .antMatchers("/js/**")
//                .antMatchers("/images/**")
//                .antMatchers("/webjars/**")
//                .antMatchers("**/favicon.ico")
//        ;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //在内存中初始化了2个用户与对应的权限（UserDetailsServiceImpl做了类似的事情,如同两者同时
//        // 存在则UserDetailsService失效）
//        auth.inMemoryAuthentication()
//                .withUser("reader").password("reader").authorities("FOO_READ")
//                .and()
//                .withUser("writer").password("writer").authorities("FOO_READ", "FOO_WRITE");
//    }
//
////    @Override
////    protected void configure(HttpSecurity http) throws Exception {
////        http
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
////                .authorizeRequests()
////                .antMatchers("/oauth/token_key").permitAll()
////                .antMatchers("/hello").permitAll();
////    }
//
//    //    @Autowired
////    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////        auth.inMemoryAuthentication().withUser("reader").password("reader").authorities("FOO_READ").and()
////                .withUser("writer").password("writer").authorities("FOO_READ", "FOO_WRITE");
////        自定义查询用户的方法
////指定密码加密所使用的加密器为passwordEncoder()
////需要将密码加密后写入数据库
////        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
////        auth.eraseCredentials(false);
////        通过数据库获取用户，数据库需要有指定的表
////        auth.jdbcAuthentication(datasource);
////    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(4);
//    }
//
//
//}