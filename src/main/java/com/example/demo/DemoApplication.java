package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URISyntaxException;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		// Initialize WebSocket connection
		WebsocketClientService clientService = context.getBean(WebsocketClientService.class);
		try {
			clientService.connect();
		} catch (URISyntaxException e) {
			System.err.println("Failed to connect to WebSocket server: " + e.getMessage());
		}
	}
}