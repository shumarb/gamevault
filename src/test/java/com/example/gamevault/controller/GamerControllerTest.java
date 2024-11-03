package com.example.gamevault.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GamerControllerTest {

    @InjectMocks
    private GamerController gamerController;

    @Test
    void showGamerLoginPage_returnsGamerIndexView() {
        String viewName = gamerController.retrieveGamerIndexPage();
        assertEquals(viewName, "gamer-index");
    }

    @Test
    void showGamerLoginPage_returnsGamerHomeView() {
        String viewName = gamerController.retrieveGamerHomePage();
        assertEquals(viewName, "gamer-home");
    }

    @Test
    void showGamerLoginPage_returnsGamerLoginView() {
        String viewName = gamerController.retrieveGamerLoginPage();
        assertEquals(viewName, "gamer-login");
    }

    @Test
    void showGamerLoginPage_returnsGamerRegistrationView() {
        String viewName = gamerController.retrieveGamerRegistrationPage();
        assertEquals(viewName, "gamer-registration");
    }

}
