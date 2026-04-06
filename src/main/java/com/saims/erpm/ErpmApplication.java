package com.saims.erpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing

@SpringBootApplication
public class ErpmApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpmApplication.class, args);
	}

}
