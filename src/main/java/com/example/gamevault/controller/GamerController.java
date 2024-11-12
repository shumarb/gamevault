package com.example.gamevault.controller;

import com.example.gamevault.exception.*;
import com.example.gamevault.model.Gamer;
import com.example.gamevault.model.Person;
import com.example.gamevault.model.VideoGame;
import com.example.gamevault.repository.VideoGameRepository;
import com.example.gamevault.security.PersonPrincipal;
import com.example.gamevault.service.GamerService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GamerController {
    private static final Logger logger = LogManager.getLogger(GamerController.class);

    @Autowired
    private VideoGameRepository videoGameRepository;

    @Autowired
    private GamerService gamerService;

    public GamerController(GamerService gamerService) {
        this.gamerService = gamerService;
    }

    @GetMapping("/")
    public String goToGamerIndexPage() {
        logger.info("Currently at Gamer Index page.");
        return "gamer-index";
    }

    @GetMapping("/gamer/registration")
    public String goToGamerRegistrationPage() {
        logger.info("Currently at Gamer Registration page.");
        return "gamer-registration";
    }

    @GetMapping("/gamer/cancellations")
    public String goToGamerCancellationsPage(Model model) {
        Gamer gamer = gamerService.getCurrentGamer();
        logger.info("Currently at Cancellation page of Gamer: {}", gamer);
        model.addAttribute("cancellations", gamer.getCancellationHistory());
        model.addAttribute("totalCredits", gamer.getTotalCredits());
        return "gamer-cancellations";
    }

    @GetMapping("/gamer/purchases")
    public String goToGamerPurchasesPage(Model model) {
        Gamer gamer = gamerService.getCurrentGamer();
        logger.info("Currently at Purchase page of Gamer: {}", gamer);
        model.addAttribute("purchases", gamer.getPurchaseHistory());
        model.addAttribute("totalCredits", gamer.getTotalCredits());
        return "gamer-purchases";
    }

    @GetMapping("/gamer/reservations")
    public String goToGamerReservationsPage(Model model) {
        Gamer gamer = gamerService.getCurrentGamer();
        logger.info("Currently at Reservation page of Gamer: {}", gamer);
        model.addAttribute("reservations", gamer.getReservationHistory());
        model.addAttribute("totalCredits", gamer.getTotalCredits());
        return "gamer-reservations";
    }

    @GetMapping("/gamer/home")
    public String goToGamerHomePage(Model model) {
        try {
            UserDetails personPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (personPrincipal instanceof PersonPrincipal loggedInGamer) {
                model.addAttribute("totalCredits", loggedInGamer.getTotalCredits());
                model.addAttribute("username", loggedInGamer.getUsername());
            }
            List<VideoGame> videoGames = videoGameRepository.findAll();
            model.addAttribute("videoGames", videoGames);
            logger.info("Currently at Gamer Home page.");
            return "gamer-home";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "gamer-index";
    }

    @GetMapping("/gamer/login")
    public String goToGamerLoginPage(HttpSession httpSession, Model model) {
        logger.info("Currently at Gamer Login page.");
        String error = (String) httpSession.getAttribute("error");
        if (error != null) {
            model.addAttribute("error", "Invalid username and/or password. Please try again.");
            httpSession.removeAttribute("error");
        }
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
            logger.info("Login attempt. Username: {}", username);
            PersonPrincipal loggedInGamer = gamerService.login(username, password);
            logger.info("Successful login for {}. Proceeding to Home page.", loggedInGamer.toString());
            httpSession.setAttribute("loggedInGamer", loggedInGamer);
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
