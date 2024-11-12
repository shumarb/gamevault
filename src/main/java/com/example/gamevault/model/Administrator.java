package com.example.gamevault.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "administrator")
public class Administrator extends Person {
    public Administrator(String name, String username, String email, String password) {
        super(name, username, email, password);
    }

    @Override
    public String getRole() {
        return "ADMINISTRATOR";
    }

}
