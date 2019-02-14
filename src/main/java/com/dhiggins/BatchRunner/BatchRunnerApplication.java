package com.dhiggins.BatchRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BatchRunnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchRunnerApplication.class, args);
	}

}

