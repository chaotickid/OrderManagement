package com.gens;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties
public class OrderManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApplication.class, args);
		log.debug("\n _____                      _        _____                 _          \n" +
				"|  __ \\                    (_)      /  ___|               (_)         \n" +
				"| |  \\/ ___ _ __   ___ _ __ _  ___  \\ `--.  ___ _ ____   ___  ___ ___ \n" +
				"| | __ / _ \\ '_ \\ / _ \\ '__| |/ __|  `--. \\/ _ \\ '__\\ \\ / / |/ __/ _ \\\n" +
				"| |_\\ \\  __/ | | |  __/ |  | | (__  /\\__/ /  __/ |   \\ V /| | (_|  __/\n" +
				" \\____/\\___|_| |_|\\___|_|  |_|\\___| \\____/ \\___|_|    \\_/ |_|\\___\\___| all copy rights are reserved");
	}

}
