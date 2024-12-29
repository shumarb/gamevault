package com.example.gamevault.model;

import org.junit.jupiter.api.Test;

public class PersonTest {

    @Test
    void createGamer_withValidParameters_success() {
        Person person = new Gamer("Ali Hassan", "alihassan", "ali_hassan@gmail.com", "QQaa123!");
    }

}
