package com.chat.Real_Time.Chat.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.chat.Real_Time.Chat.Application.repository")
@EntityScan("com.chat.Real_Time.Chat.Application.model")
public class RealTimeChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTimeChatApplication.class, args);
	}

}
