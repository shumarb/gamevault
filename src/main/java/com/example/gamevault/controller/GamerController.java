package com.example.gamevault.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GamerController {
    private static final Logger logger = LogManager.getLogger(GamerController.class);

    @GetMapping("/login")
    public String retrieveGamerLoginPage() {
        return "gamer-login";
    }

}
