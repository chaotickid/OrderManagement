package com.gens;

import com.gens.common.constants.Constants;
import com.gens.usermanagement.model.document.User;
import com.gens.usermanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties
public class OrderManagementApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OrderManagementApplication.class, args);
		log.debug("\n _____                      _        _____                 _          \n" +
				"|  __ \\                    (_)      /  ___|               (_)         \n" +
				"| |  \\/ ___ _ __   ___ _ __ _  ___  \\ `--.  ___ _ ____   ___  ___ ___ \n" +
				"| | __ / _ \\ '_ \\ / _ \\ '__| |/ __|  `--. \\/ _ \\ '__\\ \\ / / |/ __/ _ \\\n" +
				"| |_\\ \\  __/ | | |  __/ |  | | (__  /\\__/ /  __/ |   \\ V /| | (_|  __/\n" +
				" \\____/\\___|_| |_|\\___|_|  |_|\\___| \\____/ \\___|_|    \\_/ |_|\\___\\___| all copy rights are reserved");
	}

	@EventListener(ApplicationReadyEvent.class)
	public void createRootUser() throws Exception {
		Optional<User> userOptional = null;
		userOptional = userRepository.findByEmail("root@yopmail.com");
		if(userOptional.isPresent()){
			log.debug("Root user is already present no need to create again");
			return;
		}
		User rootUser =  new User();
		rootUser.setEmail("root@yopmail.com");
		rootUser.setPassword(passwordEncoder.encode("root123"));
		rootUser.setRole(Constants.ROOT);
		userRepository.save(rootUser);
	}
}
