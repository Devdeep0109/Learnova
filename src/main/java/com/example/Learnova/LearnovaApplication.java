package com.example.Learnova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LearnovaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnovaApplication.class, args);
	}

}
