package com.example.gamevault.repository;

import com.example.gamevault.model.Gamer;
import com.example.gamevault.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GamerRepository extends JpaRepository<Gamer, Long> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByEmail(String email);
}
