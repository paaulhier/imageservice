package de.paulkokot.imageservice.imageserviceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ImageserviceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageserviceServerApplication.class, args);
	}

}
