package com.wwj.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by sherry on 2017/6/7.
 */
@Configuration
public class DateSource {

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl("jdbc:mysql://localhost:3306/spring-batch?useUnicode=true&characterEncoding=utf8&useSSL=false&autoReconnect=true&failOverReadOnly=false");
        datasource.setUsername("root");
        datasource.setPassword("tiger");
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        return datasource;
    }
}