package com.secondskin.babbywear;

import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class BabbywearApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabbywearApplication.class, args);
	}


//	@Bean
//	CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder encoder) {
//		return args->{
//
//			UserInfo user= UserInfo.builder().userName("user")
//					.email("user@gmail.com")
//					.password(encoder.encode("password1"))
//					.roles("ROLE_USER")
//					.build();
//
//
//
//
//		};
//	}


}
