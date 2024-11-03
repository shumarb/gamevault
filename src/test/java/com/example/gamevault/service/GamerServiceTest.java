package com.example.gamevault.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamerServiceTest {
    private static final String EMAIL_REGEX = "[a-zA-Z][\\w._-]*@[a-z]+\\.com$";
    private static final String NAME_REGEX = "^[A-Z][a-z]{2,}(?: [A-Z][a-z]{2,})* [A-Z][a-z]{1,}$";
    private static final String PASSWORD_REGEX = "^(?=(?:.*[A-Z]){3,})(?=(?:.*[a-z]){3,})(?=(?:.*\\d){2,})[A-Za-z\\d@!#$%^&*()_+={}\\[\\]:;\"'<>?,./~`-]*$";
    private static final String USERNAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*$";

    @Test
    void checkValidName_returnsTrue() {

    }
}