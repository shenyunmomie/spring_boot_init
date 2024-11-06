package com.swshenyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootInitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootInitApplication.class, args);
	}

}
