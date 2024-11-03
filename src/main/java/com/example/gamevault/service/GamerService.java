package com.example.gamevault.service;

import com.example.gamevault.exception.*;
import com.example.gamevault.model.Gamer;
import com.example.gamevault.model.Person;
import com.example.gamevault.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GamerService {
    private static final Logger logger = LogManager.getLogger(GamerService.class);
    private static final String EMAIL_REGEX = "[a-zA-Z][\\w._-]*@[a-z]+\\.com$";
    private static final String NAME_REGEX = "^[A-Z][a-z]{2,}(?: [A-Z][a-z]{2,})* [A-Z][a-z]{1,}$";
    private static final String PASSWORD_REGEX = "^(?=(?:.*[A-Z]){3,})(?=(?:.*[a-z]){3,})(?=(?:.*\\d){2,})[A-Za-z\\d@!#$%^&*()_+={}\\[\\]:;\"'<>?,./~`-]*$";
    private static final String USERNAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*$";

    @Autowired
    private PersonRepository personRepository;

    public GamerService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person register(String name, String username, String email, String password)
            throws InvalidNameException, InvalidUsernameException, InvalidEmailAddressException, InvalidPasswordException, UnavailableUsernameException, UnavailableEmailAddressException {
        if (!isValidName(name)) {
            logger.error("Unsuccessful registration due to invalid name: {}", name);
            throw new InvalidNameException();

        } else if (!isValidUsername(username)) {
            logger.error("Unsuccessful registration due to invalid username: {}", username);
            throw new InvalidUsernameException();

        } else if (!isValidEmailAddress(email)) {
            logger.error("Unsuccessful registration due to invalid email address: {}", email);
            throw new InvalidEmailAddressException();

        } else if (!isValidPassword(password)) {
            logger.error("Unsuccessful registration due to invalid password.");
            throw new InvalidPasswordException();

        } else if (!isAvailableUsername(username)) {
            logger.error("Unsuccessful registration due to unavailable username: {}", username);
            throw new UnavailableUsernameException();

        } else if (!isAvailableEmailAddress(email)) {
            logger.error("Unsuccessful registration due to unavailable email address: {}", email);
            throw new UnavailableEmailAddressException();
        }

        Person gamer = new Gamer(name, username, email, password);
        personRepository.save(gamer);
        logger.info("Successful registration of Gamer: {}", gamer.toString());
        return gamer;
    }

    private boolean isAvailableEmailAddress(String email) {
        return personRepository.findByEmail(email).isEmpty();
    }

    private boolean isAvailableUsername(String username) {
        return personRepository.findByUsername(username).isEmpty();
    }

    private boolean isValidPassword(String password) {
        logger.info("Checking validity of password.");
        if (!password.matches(PASSWORD_REGEX)) {
            logger.error("Invalid password: {}", password);
            return false;
        }
        logger.info("Valid password: {}", password);
        return true;
    }

    private boolean isValidEmailAddress(String email) {
        logger.info("Checking validity of email address: {}", email);
        if (!email.matches(EMAIL_REGEX)) {
            logger.error("Invalid email address: {}", email);
            return false;
        }
        logger.info("Valid email address: {}", email);
        return true;
    }

    private boolean isValidUsername(String username) {
        logger.info("Checking validity of username: {}", username);
        if (username.length() < 8 || !username.matches(USERNAME_REGEX)) {
            logger.error("Invalid username: {}", username);
            return false;
        }
        logger.info("Valid username: {}", username);
        return true;
    }

    private boolean isValidName(String name) {
        logger.info("Checking validity of name: {}", name);
        if (!name.matches(NAME_REGEX)) {
            logger.error("Invalid name: {}", name);
            return false;
        }
        logger.info("Valid name: {}", name);
        return true;
    }

    public Person login(String username, String password) throws UnsuccessfulLoginException {
        logger.info("Validating login credentials (username & password). Username: {}", username);
        Optional<Person> personOptional = personRepository.findByEmail(username);
        if (personOptional.isEmpty()) {
            logger.error("Unsuccessful login as username is not found: {}", username);
            throw new UnsuccessfulLoginException();
        }
        Person person = personOptional.get();
        if (!person.getPassword().equals(password)) {
            logger.error("Unsuccessful login due to incorrect password: {}", password);
            throw new UnsuccessfulLoginException();
        }
        logger.info("Successful login | {}", person.toString());
        return person;
    }

}
