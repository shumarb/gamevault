package com.example.gamevault.security;

import com.example.gamevault.model.Person;
import com.example.gamevault.repository.GamerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonAuthenticationService implements UserDetailsService {

    @Autowired
    private GamerRepository gamerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> userOptional = gamerRepository.findByUsername(username);
        Person person = userOptional.orElseThrow(() -> new UsernameNotFoundException("Person not found"));
        return new PersonPrincipal(person);
    }
}
