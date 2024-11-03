package com.example.gamevault.controller;

import com.example.gamevault.exception.*;
import com.example.gamevault.model.Gamer;
import com.example.gamevault.model.Person;
import com.example.gamevault.service.GamerService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GamerControllerTest {

    @Mock
    private GamerService gamerService;

    @Mock
    private HttpSession httpSession;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    GamerController gamerController;

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
        gamerController = new GamerController(gamerService);
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
    void showGamerLoginPage_returnsGamerLoginView() {
        String viewName = gamerController.retrieveGamerLoginPage();
        assertEquals(viewName, "gamer-login");
    }

    @Test
    void showGamerLoginPage_doesNotReturnGamerHomeView() {
        String viewName = gamerController.retrieveGamerLoginPage();
        assertNotEquals(viewName, "gamer-home");
    }

    @Test
    void showGamerLoginPage_doesNotReturnGamerGamerRegistrationView() {
        String viewName = gamerController.retrieveGamerLoginPage();
        assertNotEquals(viewName, "gamer-registration");
    }

    @Test
    void retrieveGamerRegistrationPage_returnsGamerGamerRegistrationView() {
        String viewName = gamerController.retrieveGamerRegistrationPage();
        assertEquals(viewName, "gamer-registration");
    }

    @Test
    void retrieveGamerRegistrationPage_doesNotReturnGamerHomeView() {
        String viewName = gamerController.retrieveGamerRegistrationPage();
        assertNotEquals(viewName, "gamer-home");
    }

    @Test
    void retrieveGamerRegistrationPage_doesNotReturnGamerIndexView() {
        String viewName = gamerController.retrieveGamerRegistrationPage();
        assertNotEquals(viewName, "gamer-index");
    }

    @Test
    void retrieveGamerRegistrationPage_doesNotReturnGamerLoginView() {
        String viewName = gamerController.retrieveGamerRegistrationPage();
        assertNotEquals(viewName, "gamer-login");
    }

    @Test
    void registerGamer_withInvalidName_returnsGamerRegistrationView() throws InvalidNameException, InvalidUsernameException, InvalidEmailAddressException, InvalidPasswordException, UnavailableUsernameException, UnavailableEmailAddressException {
        doThrow(new InvalidNameException()).when(gamerService).register(invalidName, validUsername, validEmail, validPassword);
        String viewName = gamerController.register(invalidName, validUsername, validEmail, validPassword, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/registration");
        assertThrows(InvalidNameException.class, () -> gamerService.register(invalidName, validUsername, validEmail, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid name entered. Please enter a valid name.");
    }

    @Test
    void registerGamer_withInvalidUsername_returnsGamerRegistrationView() throws InvalidNameException, InvalidUsernameException, InvalidEmailAddressException, InvalidPasswordException, UnavailableUsernameException, UnavailableEmailAddressException {
        doThrow(new InvalidUsernameException()).when(gamerService).register(validName, invalidUsername, validEmail, validPassword);
        String viewName = gamerController.register(validName, invalidUsername, validEmail, validPassword, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/registration");
        assertThrows(InvalidUsernameException.class, () -> gamerService.register(validName, invalidUsername, validEmail, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid username entered. Please enter a valid username.");
    }

    @Test
    void registerGamer_withInvalidEmailAddress_returnsGamerRegistrationView() throws InvalidNameException, InvalidUsernameException, InvalidEmailAddressException, InvalidPasswordException, UnavailableUsernameException, UnavailableEmailAddressException {
        doThrow(new InvalidEmailAddressException()).when(gamerService).register(validName, validUsername, invalidEmail, validPassword);
        String viewName = gamerController.register(validName, validUsername, invalidEmail, validPassword, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/registration");
        assertThrows(InvalidEmailAddressException.class, () -> gamerService.register(validName, validUsername, invalidEmail, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid email address entered. Please enter a valid email address.");
    }

    @Test
    void registerGamer_withInvalidPassword_returnsGamerRegistrationView() throws InvalidNameException, InvalidEmailAddressException, InvalidUsernameException, InvalidPasswordException, UnavailableUsernameException, UnavailableEmailAddressException {
        doThrow(new InvalidPasswordException()).when(gamerService).register(validName, validUsername, validEmail, invalidPassword);
        String viewName = gamerController.register(validName, validUsername, validEmail, invalidPassword, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/registration");
        assertThrows(InvalidPasswordException.class, () -> gamerService.register(validName, validUsername, validEmail, invalidPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid password entered. Please enter a valid password.");
    }

    @Test
    void registerGamer_withUnavailableUsername_returnsGamerRegistrationView() throws InvalidNameException, InvalidEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException, UnavailableEmailAddressException {
        doThrow(new UnavailableUsernameException()).when(gamerService).register(validName, validUsername, validEmail, validPassword);
        String viewName = gamerController.register(validName, validUsername, validEmail, validPassword, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/registration");
        assertThrows(UnavailableUsernameException.class, () -> gamerService.register(validName, validUsername, validEmail, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Username entered is unavailable. Please enter another username.");
    }

    @Test
    void registerGamer_withUnavailableEmailAddress_returnsGamerRegistrationView() throws InvalidNameException, InvalidEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException, UnavailableEmailAddressException {
        doThrow(new UnavailableEmailAddressException()).when(gamerService).register(validName, validUsername, validEmail, validPassword);
        String viewName = gamerController.register(validName, validUsername, validEmail, validPassword, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/registration");
        assertThrows(UnavailableEmailAddressException.class, () -> gamerService.register(validName, validUsername, validEmail, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Email address entered is unavailable. Please enter another email address.");
    }

    @Test
    void registerGamer_withValidRegistrationCredentials_andUnexpectedErrorThrown_returnsGamerLoginView() throws InvalidNameException, InvalidEmailAddressException, UnavailableEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException {
        doThrow(new RuntimeException()).when(gamerService).register(validName, validUsername, validEmail, validPassword);
        String viewName = gamerController.register(validName, validUsername, validEmail, validPassword, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/registration");
        assertThrows(RuntimeException.class, () -> gamerService.register(validName, validUsername, validEmail, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Unexpected error occurred. Please try again later.");
    }

    @Test
    void registerGamer_withValidAndAvailableCredentials_returnsGamerLoginView() throws InvalidNameException, InvalidEmailAddressException, UnavailableEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException {
        Person gamer = new Gamer(validName, validUsername, validEmail, validPassword);
        when(gamerService.register(validName, validUsername, validEmail, validPassword)).thenReturn(gamer);
        String viewName = gamerController.register(validName, validUsername, validEmail, validPassword, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/login");
        verify(gamerService).register(validName, validUsername, validEmail, validPassword);
        verify(redirectAttributes).addFlashAttribute("success", "Registration successful. Please log in.");
    }

    @Test
    void loginGamer_withValidUsernameAndInvalidPassword_returnsGamerLoginPage_withCorrectErrorMessageDisplayed() throws UnsuccessfulLoginException {
        doThrow(UnsuccessfulLoginException.class).when(gamerService).login(validUsername, invalidPassword);
        String viewName = gamerController.login(validUsername, invalidPassword, httpSession, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/login");
        assertThrows(UnsuccessfulLoginException.class, () -> gamerService.login(validUsername, invalidPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid username and/or password. Please try again.");
    }

    @Test
    void loginGamer_withValidUsernameAndInvalidPassword_doesNotReturnGamerHomePage() throws UnsuccessfulLoginException {
        doThrow(UnsuccessfulLoginException.class).when(gamerService).login(validUsername, invalidPassword);
        String viewName = gamerController.login(validUsername, invalidPassword, httpSession, redirectAttributes);
        assertNotEquals(viewName, "redirect:/gamer/home");
        assertThrows(UnsuccessfulLoginException.class, () -> gamerService.login(validUsername, invalidPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid username and/or password. Please try again.");
    }

    @Test
    void loginGamer_withInvalidEmailAddressAndValidPassword_returnsGamerLoginPage_withCorrectErrorMessageDisplayed() throws UnsuccessfulLoginException {
        doThrow(UnsuccessfulLoginException.class).when(gamerService).login(invalidEmail, validPassword);
        String viewName = gamerController.login(invalidEmail, validPassword, httpSession, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/login");
        assertThrows(UnsuccessfulLoginException.class, () -> gamerService.login(invalidEmail, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid username and/or password. Please try again.");
    }

    @Test
    void loginGamer_withInvalidEmailAddressAndValidPassword_doesNotReturnGamerHomePage() throws UnsuccessfulLoginException {
        doThrow(UnsuccessfulLoginException.class).when(gamerService).login(invalidEmail, validPassword);
        String viewName = gamerController.login(invalidEmail, validPassword, httpSession, redirectAttributes);
        assertNotEquals(viewName, "redirect:/gamer/home");
        assertThrows(UnsuccessfulLoginException.class, () -> gamerService.login(invalidEmail, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid username and/or password. Please try again.");
    }

    @Test
    void loginGamer_withInvalidEmailAddressAndInvalidPassword_returnsGamerLoginPage_withCorrectErrorMessageDisplayed() throws UnsuccessfulLoginException {
        doThrow(UnsuccessfulLoginException.class).when(gamerService).login(invalidEmail, invalidPassword);
        String viewName = gamerController.login(invalidEmail, invalidPassword, httpSession, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/login");
        assertThrows(UnsuccessfulLoginException.class, () -> gamerService.login(invalidEmail, invalidPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid username and/or password. Please try again.");
    }

    @Test
    void loginGamer_withInvalidEmailAddressAndInvalidPassword_doesNotReturnGamerHomePage() throws UnsuccessfulLoginException {
        doThrow(UnsuccessfulLoginException.class).when(gamerService).login(invalidEmail, invalidPassword);
        String viewName = gamerController.login(invalidEmail, invalidPassword, httpSession, redirectAttributes);
        assertNotEquals(viewName, "redirect:/gamer/home");
        assertThrows(UnsuccessfulLoginException.class, () -> gamerService.login(invalidEmail, invalidPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Invalid username and/or password. Please try again.");
    }

    @Test
    void loginGamer_withValidUsernameAndValidPassword_andUnexpectedErrorThrown_returnsGamerLoginPage_withCorrectErrorMessageDisplayed() throws UnsuccessfulLoginException {
        doThrow(new RuntimeException()).when(gamerService).login(validUsername, validPassword);
        String viewName = gamerController.login(validUsername, validPassword, httpSession, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/login");
        assertThrows(Exception.class, () -> gamerService.login(validUsername, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Unexpected error occurred. Please try again later.");
    }

    @Test
    void loginGamer_withValidUsernameAndValidPassword_andUnexpectedErrorThrown_doesNotReturnsHomePage() throws UnsuccessfulLoginException {
        doThrow(new RuntimeException()).when(gamerService).login(validUsername, validPassword);
        String viewName = gamerController.login(validUsername, validPassword, httpSession, redirectAttributes);
        assertNotEquals(viewName, "redirect:/gamer/home");
        assertThrows(Exception.class, () -> gamerService.login(validUsername, validPassword));
        verify(redirectAttributes).addFlashAttribute("error", "Unexpected error occurred. Please try again later.");
    }

    @Test
    void loginGamer_withValidUsernameAndValidPassword_returnsHomePage() throws UnsuccessfulLoginException {
        Person gamer = new Gamer("Jamal Hassan", "jamalhassan876", validEmail, validPassword);
        when(gamerService.login(validUsername, validPassword)).thenReturn(gamer);
        String viewName = gamerController.login(validUsername, validPassword, httpSession, redirectAttributes);
        assertEquals(viewName, "redirect:/gamer/home");
    }

    @Test
    void loginGamer_withValidUsernameAndValidPassword_doesNotReturnGamerLoginPage() throws UnsuccessfulLoginException {
        Person gamer = new Gamer("Jamal Hassan", "jamalhassan876", validEmail, validPassword);
        when(gamerService.login(validUsername, validPassword)).thenReturn(gamer);
        String viewName = gamerController.login(validUsername, validPassword, httpSession, redirectAttributes);
        assertNotEquals(viewName, "redirect:/gamer/login");
    }

}
