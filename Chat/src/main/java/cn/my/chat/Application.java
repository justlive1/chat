package cn.my.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

import cn.my.chat.service.AccountService;
import cn.my.chat.service.MessageService;

@SpringBootApplication(scanBasePackages={"cn.my.chat"})
@PropertySource("config/config.properties")
@EnableAsync
public class Application {

	public static void main(String[] args) {
		
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		
		AccountService accountService = ctx.getBean(AccountService.class);
		
		accountService.login("user", "password");
		accountService.login("Hello", "world");
		
		ctx.getBean(MessageService.class).sendToOne("user", "Hello", "msg");
		
	}
}
