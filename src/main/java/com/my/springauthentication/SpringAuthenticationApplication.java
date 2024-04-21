package com.my.springauthentication;

import com.my.springauthentication.model.Role;
import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringAuthenticationApplication implements CommandLineRunner {

	@Value("${admin.firstname}")
	public String ADMIN_FIRSTNAME;
	@Value("${admin.email}")
	public String ADMIN_EMAIL;
	@Value("${admin.password}")
	public String ADMIN_PASSWORD;

	@Autowired
	private UserRepository userRepository;


	public static void main(String[] args) {
		SpringApplication.run(SpringAuthenticationApplication.class, args);
	}

	public void run(String... args) {
		User adminAccount  = userRepository.findByRole(Role.ADMIN);
		if(null == adminAccount) {
			User user = new User();

			user.setFirstname(ADMIN_FIRSTNAME);
			user.setEmail(ADMIN_EMAIL);
			user.setPassword(new BCryptPasswordEncoder().encode(ADMIN_PASSWORD));
			user.setRole(Role.ADMIN);
			user.setValidated(true);
			userRepository.save(user);
		}
	}
}
