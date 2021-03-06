package com.wwj.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 *
 */
@Configuration
@Profile("druid")
public class DruidDBConfig {
    private Logger logger = LoggerFactory.getLogger(DruidDBConfig.class);
	
    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;
    
    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;
    
    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;
    
    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;
    
    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    
    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    
    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;
    
    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;
    
    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;
    
    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;
    
    @Value("${spring.datasource.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    
    @Value("${spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    
    @Value("${spring.datasource.druid.filters}")
    private String filters;
    
    @Value("{spring.datasource.druid.connectionProperties}")
    private String connectionProperties;
    
    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource(){
        logger.info("注入druid");
    	DruidDataSource datasource = new DruidDataSource();
    	
    	datasource.setUrl(dbUrl);
    	datasource.setUsername(username);
    	datasource.setPassword(password);
    	datasource.setDriverClassName(driverClassName);
    	
    	//configuration
    	datasource.setInitialSize(initialSize);
    	datasource.setMinIdle(minIdle);
    	datasource.setMaxActive(maxActive);
    	datasource.setMaxWait(maxWait);
    	datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    	datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    	datasource.setValidationQuery(validationQuery);
    	datasource.setTestWhileIdle(testWhileIdle);
    	datasource.setTestOnBorrow(testOnBorrow);
    	datasource.setTestOnReturn(testOnReturn);
    	datasource.setPoolPreparedStatements(poolPreparedStatements);
    	datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
    	try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			logger.error("druid configuration initialization filter", e);
		}
    	datasource.setConnectionProperties(connectionProperties);
    	
    	return datasource;
    }

//    private org.apache.tomcat.jdbc.pool.DataSource pool;
//	@Bean(name = "datasource2",destroyMethod = "close")
//	public DataSource dataSource() {
//        this.pool = new org.apache.tomcat.jdbc.pool.DataSource();
//		pool.setDriverClassName(driverClassName);
//		pool.setUrl(dbUrl);
//        pool.setUsername(username);
//        pool.setPassword(password);
//		pool.setInitialSize(initialSize);
//		pool.setMaxActive(maxActive);
//		pool.setMinIdle(minIdle);
//		pool.setMaxIdle(maxWait);
//		pool.setTestOnBorrow(testOnBorrow);
//		pool.setTestOnReturn(testOnReturn);
//		pool.setValidationQuery(validationQuery);
//		return pool;
//	}
//
//	@PreDestroy
//	public void close() {
//		if (this.pool != null) {
//			this.pool.close();
//		}
//	}

}
