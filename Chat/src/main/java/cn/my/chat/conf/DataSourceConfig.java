package cn.my.chat.conf;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@MapperScan("cn.my.chat.dao")
public class DataSourceConfig {

	@Bean("h2DataSource")
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
