package cn.my.chat.conf;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * dataSource配置类<br>
 * 用于配置数据源、mybatis、事务
 * @author WB
 *
 */

@Configuration
@MapperScan("cn.my.chat.dao")
public class DataSourceConfig {

	@Bean
	@Profile("dev")
	public DataSource h2DataSource() {

		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.setScriptEncoding("utf-8")
				.ignoreFailedDrops(true)
				.addScript("/config/schema.sql")
				.addScript("/config/init-data.sql")
				.build();
	}
	
	@Bean
	@Profile("pro")
	public DataSource mysqlDataSource(DataSourceProperties properties){
		
		return properties.initializeDataSourceBuilder().build();
	}
	
	@Bean(initMethod = "afterPropertiesSet")
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {

		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);

		return factory;
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {

		return new DataSourceTransactionManager(dataSource);
	}
}
