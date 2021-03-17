package com.randomprogramming.explorer.controllers;

import com.randomprogramming.explorer.models.LocationModel;
import com.randomprogramming.explorer.services.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    final private LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<String> addLocation(@RequestBody LocationModel model) {
        if (model.hasNullValues())
            return new ResponseEntity<>("Information missing, please try again.", HttpStatus.BAD_REQUEST);

        if (locationService.addLocation(model))
            return new ResponseEntity<>("Location added.", HttpStatus.OK);
        else
            return new ResponseEntity<>(
                    "Failed to add location, please try again.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
