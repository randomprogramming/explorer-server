package com.randomprogramming.explorer.controllers;

import com.randomprogramming.explorer.models.LocationModel;
import com.randomprogramming.explorer.models.RegionModel;
import com.randomprogramming.explorer.services.LocationService;
import com.randomprogramming.explorer.services.PersonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/location")
@Log4j2
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

    @GetMapping
    public ResponseEntity<?> searchLocations(@RequestParam String searchQuery) {
        if (searchQuery == null || searchQuery.length() == 0) {
            return new ResponseEntity<>("Search query is missing.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(locationService.searchLocations(searchQuery), HttpStatus.OK);
    }

    @GetMapping("/like/{id}")
    public ResponseEntity<String> markLocationAsLiked(@PathVariable String id, HttpServletRequest req) {
        if (locationService.markLocationAsLiked(id, personService.getUsernameFromRequest(req)))
            return new ResponseEntity<>("Location liked!", HttpStatus.OK);
        else
            return new ResponseEntity<>(
                    "Failed to like location, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/dislike/{id}")
    public ResponseEntity<String> removeLikeFromLocation(@PathVariable String id, HttpServletRequest req) {
        if (locationService.removeLikeFromLocation(id, personService.getUsernameFromRequest(req)))
            return new ResponseEntity<>("Location unliked!", HttpStatus.OK);
        else
            return new ResponseEntity<>(
                    "Failed to unlike location, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/liked") //TODO: add a added date, when person liked location and sort by it
    public ResponseEntity<?> getLikedLocations(HttpServletRequest req) {
        try {
            return new ResponseEntity<>(
                    locationService.getLikedLocations(personService.getUsernameFromRequest(req)), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            log.error(e);
            return new ResponseEntity<>(
                    "Error when checking for liked locations, please try again.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/liked/{id}")
    public ResponseEntity<Boolean> checkIfLocationIsLiked(@PathVariable String id, HttpServletRequest req) {
        // Checks if the user liked the location with the provided id
        return new ResponseEntity<>(
                locationService.hasUserLikedLocation(id, personService.getUsernameFromRequest(req)), HttpStatus.OK);
    }

    @PostMapping("/region")
    public ResponseEntity<?> searchRegionForLocations(@RequestBody RegionModel model) {
        return new ResponseEntity<>(locationService.searchRegionForLocations(model), HttpStatus.OK);
    }
}
