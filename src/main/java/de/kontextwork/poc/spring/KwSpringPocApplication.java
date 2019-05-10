package de.kontextwork.poc.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
		basePackages = "de.kontextwork.poc.spring"
)
public class KwSpringPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(KwSpringPocApplication.class, args);
		var i = 1;
	}

}
