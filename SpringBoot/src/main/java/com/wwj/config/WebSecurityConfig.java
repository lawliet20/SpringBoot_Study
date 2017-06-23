package com.wwj.config;

import com.wwj.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;

    //参考网址：http://blog.csdn.net/u012702547/article/details/54319508
    //http://localhost:8080/login 输入正确的用户名密码 并且选中remember-me 则登陆成功，转到 index页面
    //再次访问index页面无需登录直接访问
    //访问http://localhost:8080/home 不拦截，直接访问，
    //访问http://localhost:8080/account 需要登录验证后，且具备 “ADMIN”权限hasAuthority("ADMIN")才可以访问
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        http
//                .csrf()
//                .disable()
//                .authorizeRequests()
//                .antMatchers("/","/home").permitAll()//访问：无需登录认证权限
//                .antMatchers("/test/restTemplateRes").permitAll()
//                .anyRequest().authenticated() //其他所有资源都需要认证，登陆后访问
//                .antMatchers("/account/**").hasAuthority("ADMIN") //登陆后之后拥有“ADMIN”权限才可以访问/hello方法，否则系统会出现“403”权限不足的提示
//                .and()
//                .formLogin()
//                .loginPage("/login")//指定登录页是”/login”
//                .permitAll()
//                .successHandler(loginSuccessHandler()) //登录成功后可使用loginSuccessHandler()存储用户信息，可选。
//                .and()
//                .logout()
//                .permitAll()
//                .invalidateHttpSession(true);
//                .and()
        //开启cookie保存用户数据
//                .rememberMe()
//                //设置cookie有效期
//                .tokenValiditySeconds(60 * 60 * 24 * 7)
//                //设置cookie的私钥
//                .key("")
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    /**
     * 一般用于设置不需要进行权限过滤的静态资源
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/pages/**");
        super.configure(web);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("wwj").password("123").roles("USER");
//指定密码加密所使用的加密器为passwordEncoder()
//需要将密码加密后写入数据库
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//        auth.eraseCredentials(false);
    }

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(4);
//    }
//
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}