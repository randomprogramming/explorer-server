package com.randomprogramming.explorer.controllers;

import com.randomprogramming.explorer.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    final private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/me")
    public ResponseEntity<String> getMe(HttpServletRequest req) {
        return new ResponseEntity<>(personService.getUsernameFromRequest(req), HttpStatus.OK);
    }
}
