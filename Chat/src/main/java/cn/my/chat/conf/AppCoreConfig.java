package cn.my.chat.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import io.vertx.core.Vertx;

@Configuration
public class AppCoreConfig {

	@Bean
	public Vertx vertx() {

		return Vertx.vertx();
	}
	
	@Bean
	public TaskExecutor executor() {

		return new SimpleAsyncTaskExecutor("spring-async-task-");
	}
}
