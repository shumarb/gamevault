package com.example.gamevault.controller;

import com.example.gamevault.exception.*;
import com.example.gamevault.model.Person;
import com.example.gamevault.service.GamerService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GamerController {
    private static final Logger logger = LogManager.getLogger(GamerController.class);

    @Autowired
    private GamerService gamerService;

    public GamerController(GamerService gamerService) {
        this.gamerService = gamerService;
    }

    @GetMapping("/")
    public String retrieveGamerIndexPage() {
        logger.info("Currently at Gamer Index page.");
        return "gamer-index";
    }

    @GetMapping("/gamer/registration")
    public String retrieveGamerRegistrationPage() {
        logger.info("Currently at Gamer Registration page.");
        return "gamer-registration";
    }

    @GetMapping("/gamer/home")
    public String retrieveGamerHomePage() {
        logger.info("Currently at Gamer Home page.");
        return "gamer-home";
    }

    @GetMapping("/gamer/login")
    public String retrieveGamerLoginPage() {
        logger.info("Currently at Gamer Login page.");
        return "gamer-login";
    }

    @PostMapping("/gamer/registration")
    public String register(@RequestParam("name") String name,
                           @RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           RedirectAttributes redirectAttributes) {
        try {
            logger.info("Registration attempt. Name: {}, Username: {}, Email Address: {}, Password: {}", name, username, email, password);
            Person gamer = gamerService.register(name, username, email, password);
            logger.info("Successful registration of {}", gamer.toString());
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please log in.");
            return "redirect:/gamer/login";

        } catch (InvalidNameException e) {
            handleUnsuccessfulRegistration("error", "Unsuccessful registration due to invalid name: " + name, redirectAttributes, "Invalid name entered. Please enter a valid name.");

        } catch (InvalidUsernameException e) {
            handleUnsuccessfulRegistration("error", "Unsuccessful registration due to invalid username: " + username, redirectAttributes, "Invalid username entered. Please enter a valid username.");

        } catch (InvalidEmailAddressException e) {
            handleUnsuccessfulRegistration("error", "Unsuccessful registration due to invalid email address: " + email, redirectAttributes, "Invalid email address entered. Please enter a valid email address.");

        } catch (InvalidPasswordException e) {
            handleUnsuccessfulRegistration("error", "Unsuccessful registration due to invalid password: " + password, redirectAttributes, "Invalid password entered. Please enter a valid password.");

        } catch (UnavailableUsernameException e) {
            handleUnsuccessfulRegistration("error", "Unsuccessful registration due to unavailable username: " + username, redirectAttributes, "Username entered is unavailable. Please enter another username.");

        } catch (UnavailableEmailAddressException e) {
            handleUnsuccessfulRegistration("error", "Unsuccessful registration due to unavailable email address: " + email, redirectAttributes, "Email address entered is unavailable. Please enter another email address.");

        } catch (Exception e) {
            handleUnsuccessfulRegistration("fatal", "Unsuccessful registration due to unexpected error. ", redirectAttributes,"Unexpected error occurred. Please try again later.");
        }

        return "redirect:/gamer/registration";
    }

    private void handleUnsuccessfulRegistration(String logLevel, String logMessage, RedirectAttributes redirectAttributes, String errorMessageToDisplay) {
        if (logLevel.equals("error")) {
            logger.error(logMessage);
        } else if (logLevel.equals("fatal")) {
            logger.fatal(logMessage);
        }
        redirectAttributes.addFlashAttribute("error", errorMessageToDisplay);
    }

    @PostMapping("/gamer/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession httpSession,
                        RedirectAttributes redirectAttributes) {
        try {
            Person loggedInGamer = gamerService.login(username, password);
            httpSession.setAttribute("loggedInGamer", loggedInGamer);
            logger.info("Successful login for {}. Proceeding to Home page.", loggedInGamer.toString());
            return "redirect:/gamer/home";

        } catch (UnsuccessfulLoginException e) {
            e.printStackTrace();
            handleUnsuccessfulLogin("error", redirectAttributes, "Invalid username and/or password. Please try again.");

        } catch (Exception e) {
            e.printStackTrace();
            handleUnsuccessfulLogin("fatal", redirectAttributes, "Unexpected error occurred. Please try again later.");
        }

        return "redirect:/gamer/login";
    }

    private void handleUnsuccessfulLogin(String logLevel, RedirectAttributes redirectAttributes, String errorMessageToDisplay) {
        if (logLevel.equals("error")) {
            logger.error("Unsuccessful login due to invalid username and/or password. Redirection to Login page with error message displayed.");
        } else if (logLevel.equals("fatal")) {
            logger.fatal("Unsuccessful login due to unexpected error.");
        }
        redirectAttributes.addFlashAttribute("error", errorMessageToDisplay);
    }

}
