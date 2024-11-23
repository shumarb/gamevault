/**
 * Abstract class serving as a base class for different types of users of the application.
 */
package com.example.gamevault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
public abstract class Person {

    /**
     * Identification number attribute of a Person entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Name attribute of a Person entity.
     */
    private String name;

    /**
     * Username attribute of a Person entity.
     */
    @Column(unique = true)
    private String username;

    /**
     * Email address attribute of a Person entity.
     */
    @Column(unique = true)
    private String email;

    /**
     * Password attribute of a Person entity.
     */
    private String password;

    /**
     * Constructor to instantiate a Person entity.
     * @param name      The name of the Person entity.
     * @param username  The username of the Person entity.
     * @param email     The email address of the Person entity.
     * @param password  The password of the Person entity.
     */
    public Person(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Abstract method to get the role of the Person entity, to be implemented by its subclasses.
     * @return The role of the Person entity.
     */
    public abstract String getRole();

}
