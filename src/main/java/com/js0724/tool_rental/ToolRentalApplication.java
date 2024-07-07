package com.js0724.tool_rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ToolRentalApplication {

	public static void main(String[] args) {
		// I prefer using ENV's since this standard accross frameworks/languages
		Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
		
		SpringApplication.run(ToolRentalApplication.class, args);
	}

}
