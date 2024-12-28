package com.example.gamevault.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class AdministratorControllerTest {

    @Mock
    private AdministratorController administratorController;

    @Mock
    private AdministratorService administratorService;

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
        administratorController = new AdministratorController(administratorService);
        validName = "John Tan";
        validUsername = "john_gamevault";
        validEmail = "john.tan@gamevault.com";
        validPassword = "QQQaaa123";
        invalidName = "John";
        invalidUsername = "john123";
        invalidEmail = "johntan@gmail.com";
        invalidPassword = "ada";
    }

    @Test
    void showAdministratorLoginPage_returnsAdministratorLoginPage() {
        String viewName = administratorController.retrieveAdministratorLoginPage();
        assertEquals(viewName, "administrator-login");
    }

    @Test
    void showAdministratorLoginPage_returnsAdministratorHomePage() {
        String viewName = administratorController.retrieveAdministratorHomePage();
        assertEquals(viewName, "administrator-home");
    }

    @Test
    void showAdministratorLoginPage_returnsAdministratorIndexPage() {
        String viewName = administratorController.retrieveAdministratorIndexPage();
        assertEquals(viewName, "administrator-index");
    }

    @Test
    void showAdministratorLoginPage_returnsAdministratorRegistrationPage() {
        String viewName = administratorController.retrieveAdministratorRegistrationPage();
        assertEquals(viewName, "administrator-registration");
    }

    /*@Test
    void registerAdministrator_withInvalidName_returnsAdministratorRegistrationPage_withErrorMessageDisplayed() {
        doThrow(InvalidNameException.class).when(administratorService).register(invalidName, validUsername, validEmail, validPassword);
    }
*/
}
