package com.randomprogramming.explorer.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @PostMapping
    public ResponseEntity<String> addLocation() {
        return new ResponseEntity<>("Hello world", HttpStatus.ACCEPTED);
    }
}
