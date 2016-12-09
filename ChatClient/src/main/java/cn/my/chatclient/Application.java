package cn.my.chatclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

import cn.my.chatclient.service.OptionsService;

@SpringBootApplication(scanBasePackages = { "cn.my.chatclient" })
@PropertySource("config/config.properties")
@EnableAsync
public class Application {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		OptionsService options = ctx.getBean(OptionsService.class);
		
		options.login("user", "password");
			
	}
}
