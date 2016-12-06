package cn.my.chat.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import cn.my.chat.core.ErrorCodeMessageCodec;
import cn.my.chat.exception.ErrorCode;
import cn.my.chat.service.MessageService;
import cn.my.chat.service.NotifierService;
import cn.my.chat.service.impl.MessageServiceImpl;
import cn.my.chat.service.impl.NotifierServiceImpl;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

@Configuration
public class AppCoreConfig {

	@Bean
	public Vertx vertx() {

		return Vertx.vertx();
	}
	
	@Bean
	public EventBus vertxEventBus(Vertx vertx){
		EventBus eventBus =	vertx.eventBus();
		eventBus.registerDefaultCodec(ErrorCode.class, new ErrorCodeMessageCodec());
		return eventBus;
	}
	
	@Bean
	public TaskExecutor executor() {

		return new SimpleAsyncTaskExecutor("spring-async-task-");
	}
	
	@Bean
	@Profile("single")
	public MessageService springMessageService(){
		
		return new MessageServiceImpl();
	}
	
	@Bean
	@Profile("single")
	public NotifierService springNotifierService(){
		
		return new NotifierServiceImpl();
	}
	
}
