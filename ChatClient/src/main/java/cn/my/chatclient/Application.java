package cn.my.chatclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = { "cn.my.chatclient" })
@PropertySource("config/config.properties")
@EnableAsync
public class Application {

	public static void main(String[] args) {

		System.setProperty("java.awt.headless", "false");
		
		SpringApplication.run(Application.class, args);

	}
}