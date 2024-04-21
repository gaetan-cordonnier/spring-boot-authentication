package com.my.springauthentication;

import com.my.springauthentication.model.Role;
import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringAuthenticationApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;


	public static void main(String[] args) {
		SpringApplication.run(SpringAuthenticationApplication.class, args);
	}

	public void run(String... args) {
		User adminAccount  = userRepository.findByRole(Role.ADMIN);
		if(null == adminAccount) {
			User user = new User();

			user.setFirstname("Admin");
			user.setEmail("admin@localhost");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("@dMiN123"));
			userRepository.save(user);
		}
	}
}
