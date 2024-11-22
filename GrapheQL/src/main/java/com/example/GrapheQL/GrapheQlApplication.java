package com.example.GrapheQL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.example.GrapheQL")
public class GrapheQlApplication {
	public static void main(String[] args) {
		SpringApplication.run(GrapheQlApplication.class, args);
	}
}


