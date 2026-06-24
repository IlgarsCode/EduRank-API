package com.ilgarscode.edurank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EdurankApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				EdurankApplication.class,
				args
		);
	}
}