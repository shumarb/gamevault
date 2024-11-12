package com.example.gamevault.controller;

import com.example.gamevault.service.AdministratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorController {
    private static final Logger logger = LogManager.getLogger(GamerController.class);

    @Autowired
    private AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping("/administrator")
    public String retrieveAdministratorIndexPage() {
        return "administrator-index";
    }

    @GetMapping("/administrator/login")
    public String retrieveAdministratorLoginPage() {
        return "administrator-login";
    }

    @GetMapping("/administrator/home")
    public String retrieveAdministratorHomePage() {
        return "administrator-home";
    }

    @GetMapping("/administrator/registration")
    public String retrieveAdministratorRegistrationPage() {
        return "administrator-registration";
    }

}
