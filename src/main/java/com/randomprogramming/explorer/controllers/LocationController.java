package com.randomprogramming.explorer.controllers;

import com.randomprogramming.explorer.models.LocationModel;
import com.randomprogramming.explorer.services.LocationService;
import com.randomprogramming.explorer.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    final private LocationService locationService;

    final private PersonService personService;

    public LocationController(LocationService locationService, PersonService personService) {
        this.locationService = locationService;
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<String> addLocation(@ModelAttribute LocationModel model, HttpServletRequest req) throws IOException {
        if (model.hasNullValues())
            return new ResponseEntity<>("Information missing, please try again.", HttpStatus.BAD_REQUEST);

        if (locationService.addLocation(model, personService.getUsernameFromRequest(req)))
            return new ResponseEntity<>("Location added.", HttpStatus.OK);
        else
            return new ResponseEntity<>(
                    "Failed to add location, please try again.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @PostMapping("/like/{id}")
//    public ResponseEntity<String> likeLocation(@RequestParam String id) {
//        return new ResponseEntity<>("Hello world");
//    }
}
