package com.lbritosan.first_spring_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("dev")
public class FirstSpringAppApplication {

	public static void main(String[] args) {
        SpringApplication.run(FirstSpringAppApplication.class, args);
	}
}
