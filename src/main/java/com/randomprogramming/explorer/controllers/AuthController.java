package com.randomprogramming.explorer.controllers;

import com.randomprogramming.explorer.exceptions.InformationMissingException;
import com.randomprogramming.explorer.exceptions.PasswordException;
import com.randomprogramming.explorer.models.LoginModel;
import com.randomprogramming.explorer.models.PersonModel;
import com.randomprogramming.explorer.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;

@RestController
@RequestMapping("/api")
public class AuthController {
    final private PersonService personService;

    public AuthController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PersonModel model) {
        try {
            personService.registerPerson(model);
            return new ResponseEntity<>("Registration successful.", HttpStatus.OK);
        } catch (InformationMissingException | EntityExistsException | PasswordException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("There was an error, please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginModel model) {
        try {
            String token = personService.login(model.getUsername(), model.getPassword());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
