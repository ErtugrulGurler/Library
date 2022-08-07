package com.kitap.kitap;


import com.kitap.kitap.domain.Role;
import com.kitap.kitap.domain.User;
import com.kitap.kitap.service.RoleService;
import com.kitap.kitap.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * TODO: Users can get books and add their books in to their lists;
 * There will be counter for books; Arnold should not be able to change his role ? ;
 * If saved books have exactly the same name counter ++;
 * Users should have ROLES with MANY TO MANY ANNOTATION;
 */
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



