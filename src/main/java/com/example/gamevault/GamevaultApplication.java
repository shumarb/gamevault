package com.example.gamevault;

import com.example.gamevault.configuration.InitialStartupConfiguration;
import com.example.gamevault.exception.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GamevaultApplication {

	@Autowired
	private InitialStartupConfiguration initialStartupConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(GamevaultApplication.class, args);
	}

	@PostConstruct
	public void initialization() throws InvalidNameException, InvalidEmailAddressException, UnavailableEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException {
		initialStartupConfiguration.initialize();
	}

}
