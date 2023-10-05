package com.secondskin.babbywear;

import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
//@EnableScheduling
//@ComponentScan("com.secondskin.babbywear.service.variant")
public class BabbywearApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabbywearApplication.class, args);
	}




}
