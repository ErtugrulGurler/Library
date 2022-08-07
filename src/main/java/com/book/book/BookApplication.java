package com.book.book;

import com.book.book.domain.Role;
import com.book.book.domain.User;
import com.book.book.service.RoleService;
import com.book.book.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * TODO: Users can get books and add their books in to their lists;
 * Arnold should not be able to change his role ? ;
 */
@SpringBootApplication
public class BookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {
			roleService.saveRole(new Role(null, "USER"));
			roleService.saveRole(new Role(null, "ADMIN"));
			roleService.saveRole(new Role(null, "SUPER_ADMIN"));

			userService.saveUser(new User(null, "Arnold Schwarzenegger", "arnold", "1234", "arnold.gmail.com",true));
			userService.saveUser(new User(null, "thomas", "tom", "1234", "tommiks.gmail.com"));

			roleService.addRoleToUser("arnold", "SUPER_ADMIN");
			roleService.addRoleToUser("tom", "SUPER_ADMIN");
		};
	}
}



