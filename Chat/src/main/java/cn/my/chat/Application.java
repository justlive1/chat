package cn.my.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages={"cn.my.chat"})
@PropertySource("config/config.properties")
@EnableAsync
public class Application {

	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		
	}
}
