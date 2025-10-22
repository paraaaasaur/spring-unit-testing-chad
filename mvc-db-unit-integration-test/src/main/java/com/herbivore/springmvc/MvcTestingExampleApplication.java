package com.herbivore.springmvc;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static io.github.paraaaasaur.util.Toolbox.blue;
import static io.github.paraaaasaur.util.Toolbox.yellow;

@SpringBootApplication
public class MvcTestingExampleApplication {
	private static final Logger logger = LoggerFactory.getLogger(MvcTestingExampleApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(MvcTestingExampleApplication.class, args);
	}


	@PostConstruct
	private void aparecium() {
		logger.info(yellow("✨ Aparecium! MagicalBean is ready for action."));
	}

	@PreDestroy
	private void evanesco() {
		logger.info(blue("✨ Evanesco! MagicalBean is vanishing gracefully."));
	}
}