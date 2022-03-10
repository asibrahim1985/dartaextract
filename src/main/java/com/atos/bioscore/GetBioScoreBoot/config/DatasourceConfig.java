package com.atos.bioscore.GetBioScoreBoot.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DatasourceConfig {
	
	@Configuration(proxyBeanMethods = false)
	public class MyDataSourceConfiguration {

	    @Bean
	    @Primary
	    @ConfigurationProperties("bidb.datasource")
	    public HikariDataSource dataSource() {
	        return DataSourceBuilder.create().type(HikariDataSource.class).build();
	    }
	    
	    
	    @Bean(name="regprcDatasource")
	    @ConfigurationProperties("regprc.datasource")
	    public HikariDataSource dataSourceregprc() {
	        return DataSourceBuilder.create().type(HikariDataSource.class).build();
	    }
	    
	    @Bean(name="mvDatasource")
	    @ConfigurationProperties("mv.datasource")
	    public HikariDataSource dataSourcemv() {
	        return DataSourceBuilder.create().type(HikariDataSource.class).build();
	    }

	}

}
