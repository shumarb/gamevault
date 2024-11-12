package com.example.gamevault.service;

import com.example.gamevault.controller.GamerController;
import com.example.gamevault.exception.*;
import com.example.gamevault.model.Gamer;
import com.example.gamevault.model.Person;
import com.example.gamevault.repository.GamerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GamerServiceTest {

    @Mock
    private GamerService gamerService;

    @Mock
    private GamerRepository gamerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String validName;
    private String validUsername;
    private String validEmail;
    private String validPassword;
    private String invalidName;
    private String invalidUsername;
    private String invalidEmail;
    private String invalidPassword;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validName = "Syed Hassan";
        validUsername = "Syed";
        validEmail = "syed.hassan@gmail.com";
        validPassword = "QWQ233!";
        invalidName = "AL";
        invalidUsername = "al";
        invalidEmail = "a@@@@.ada";
        invalidPassword = "Sz";
    }

    @Test
    void registerGamer_failure_invalidName() throws InvalidNameException, InvalidEmailAddressException, UnavailableEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException {
        when(gamerService.register(invalidName, validUsername, validEmail, validPassword)).thenThrow(InvalidNameException.class);
        assertThrows(InvalidNameException.class, () -> gamerService.register(invalidName, validUsername, validEmail, validPassword));
    }

    @Test
    void registerGamer_failure_invalidUsername() throws InvalidNameException, InvalidEmailAddressException, UnavailableEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException {
        when(gamerService.register(validName, invalidUsername, validEmail, validPassword)).thenThrow(InvalidUsernameException.class);
        assertThrows(InvalidUsernameException.class, () -> gamerService.register(validName, invalidUsername, validEmail, validPassword));
    }

    @Test
    void login() {
    }

    @Test
    void canAffordTransaction() {
    }

    @Test
    void deductCredits() {
    }

    @Test
    void deductCreditsForReservation() {
    }

    @Test
    void addPurchaseTransactionForGamer() {
    }

    @Test
    void getCurrentGamer() {
    }

    @Test
    void addReservationTransactionForGamer() {
    }

    @Test
    void addCancelTransaction() {
    }

    @Test
    void removeReservationTransaction() {
    }
}