package cn.my.chat.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import cn.my.chat.service.MessageService;
import cn.my.chat.service.NotifierService;
import cn.my.chat.service.impl.MessageServiceImpl;
import cn.my.chat.service.impl.NotifierServiceImpl;
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
	
	@Bean
	@Profile("dev")
	public MessageService springMessageService(){
		
		return new MessageServiceImpl();
	}
	
	@Bean
	@Profile("dev")
	public NotifierService springNotifierService(){
		
		return new NotifierServiceImpl();
	}
	
}
