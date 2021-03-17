package com.randomprogramming.explorer.services;

import com.randomprogramming.explorer.entities.Person;
import com.randomprogramming.explorer.exceptions.InformationMissingException;
import com.randomprogramming.explorer.exceptions.PasswordException;
import com.randomprogramming.explorer.models.PersonModel;
import com.randomprogramming.explorer.repositories.PersonRepository;
import com.randomprogramming.explorer.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
public class PersonService {
    final private PersonRepository personRepository;

    final private BCryptPasswordEncoder passwordEncoder;

    final private AuthenticationManager authenticationManager;

    final private JwtTokenProvider tokenProvider;

    public PersonService(PersonRepository personRepository,
                         BCryptPasswordEncoder passwordEncoder,
                         AuthenticationManager authenticationManager,
                         JwtTokenProvider tokenProvider) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public void registerPerson(PersonModel model)
            throws InformationMissingException, EntityExistsException, PasswordException {
        if (model.hasNullValues()) {
            throw new InformationMissingException("Some information was missing, please try again.");
        }

        if (personRepository.existsByUsername(model.getUsername())
                || personRepository.existsByEmail(model.getEmail())) {
            throw new EntityExistsException("The entered username or email are already in use.");
        }

        if (model.getPassword().length() < 8) {
            throw new PasswordException("Password must be 8 characters or longer.");
        }

        if (!model.getPassword().equals(model.getRepeatedPassword())) {
            throw new PasswordException("Entered passwords don't match.");
        }

        var person = new Person(true,
                model.getEmail(),
                model.getUsername(),
                passwordEncoder.encode(model.getPassword()));

        personRepository.save(person);
    }

    public String login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.createToken(username);
    }
}
