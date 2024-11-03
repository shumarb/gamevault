package com.example.gamevault.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GamerController {
    private static final Logger logger = LogManager.getLogger(GamerController.class);

    @GetMapping("/")
    public String retrieveGamerIndexPage() {
        return "gamer-index";
    }

    @GetMapping("/home")
    public String retrieveGamerHomePage() {
        return "gamer-home";
    }

    @GetMapping("/login")
    public String retrieveGamerLoginPage() {
        return "gamer-login";
    }

    @GetMapping("/login")
    public String retrieveGamerRegistrationPage() {
        return "gamer-registration";
    }

}
