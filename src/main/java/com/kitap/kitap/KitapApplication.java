package com.kitap.kitap;


import com.kitap.kitap.domain.Role;
import com.kitap.kitap.domain.User;

import com.kitap.kitap.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class KitapApplication {

	public static void main(String[] args) {
		SpringApplication.run(KitapApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {

			userService.saveRole(new Role(null, "USER"));
			userService.saveRole(new Role(null, "ADMIN"));
			userService.saveRole(new Role(null, "SUPER_ADMIN"));

			userService.saveUser(new User(null, "Arnold Schwarzenegger", "arnold", "1234", "arnold.gmail.com",true));
			userService.saveUser(new User(null, "thomas", "tom", "1234", "tommiks.gmail.com"));

			userService.addRoleToUser("arnold", "SUPER_ADMIN");
			userService.addRoleToUser("tom", "SUPER_ADMIN");

		};
	}
}



