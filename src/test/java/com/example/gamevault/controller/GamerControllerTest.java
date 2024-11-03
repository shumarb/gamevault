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
    void showGamerLoginPage_returnsGamerLoginView() {
        String viewName = gamerController.retrieveGamerLoginPage();
        assertEquals(viewName, "gamer-login");
    }

}
