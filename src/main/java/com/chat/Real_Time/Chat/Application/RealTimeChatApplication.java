package com.chat.Real_Time.Chat.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealTimeChatApplication {

	public static void main(String[] args) {
		System.out.println("--- Debugging Environment Variables ---");
		System.out.println("SPRING_DATASOURCE_URL: " + System.getenv("SPRING_DATASOURCE_URL"));
		SpringApplication.run(RealTimeChatApplication.class, args);
	}
}