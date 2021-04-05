package com.randomprogramming.explorer.services;

import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.randomprogramming.explorer.comparators.SortPersonLikesAssociationByLikeDate;
import com.randomprogramming.explorer.entities.Location;
import com.randomprogramming.explorer.entities.Media;
import com.randomprogramming.explorer.entities.Person;
import com.randomprogramming.explorer.entities.PersonLikesAssociation;
import com.randomprogramming.explorer.models.LocationModel;
import com.randomprogramming.explorer.models.RegionModel;
import com.randomprogramming.explorer.repositories.LocationRepository;
import com.randomprogramming.explorer.repositories.PersonLikesAssociationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Log4j2
public class LocationService {
    final private PersonLikesAssociationRepository plaRepository;

    final private LocationRepository locationRepository;

    final private PersonService personService;

    public LocationService(LocationRepository locationRepository,
                           PersonService personService,
                           PersonLikesAssociationRepository personLikesAssociationRepository) {
        this.locationRepository = locationRepository;
        this.personService = personService;
        this.plaRepository = personLikesAssociationRepository;
    }

    public boolean addLocation(LocationModel model, String username) throws IOException {
        if (model.getMedia().length == 0 || model.getDescription().length() > 512) {
            // Location must have at least one picture
            // Description may not be longer than 512 chars
            return false;
        }

        var person = personService.getPersonFromUsername(username);
        if (person == null) {
            return false;
        }

        log.info("Starting file upload...");
        var cloudinary = Singleton.getCloudinary();
        Set<Media> mediaSet = new HashSet<>();

        for (MultipartFile image : model.getMedia()) {
            var response = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto",
                            "transformation", new Transformation().quality("auto")));

            String url = (String) response.get("url");
            // If the file name is equals to 0, meaning it's the first element in the array on the
            // front end, set it as the thumbnail image
            Optional<String> imageNameOptional = Optional.ofNullable(image.getOriginalFilename());
            var isThumbnail = imageNameOptional.isPresent() && imageNameOptional.get().equals("0");
            mediaSet.add(new Media(url, isThumbnail));
        }
        log.info("File upload finished");

        var location = new Location(
                model.getLatitude(), model.getLongitude(), model.getTitle(), model.getDescription(), mediaSet, person);

        try {
            locationRepository.save(location);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Page<Location> searchLocations(String searchQuery) {
        // TODO: Implement searchability by location too
        // Also maybe increase the size of the searched fields? Idk
        // TODO: Also make the search actually do something because right now you have to type the exact
        // name of the location
        return locationRepository.search(searchQuery, PageRequest.of(0, 15));
    }

    public boolean markLocationAsLiked(String locationId, String personUsername) {
        Person person = personService.getPersonFromUsername(personUsername);
        if (person == null) return false;

        Optional<Location> locationOptional = locationRepository.findFirstById(locationId);

        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();

            // If the association doesn't already exist, create it
            if (!plaRepository.existsByPersonIdAndLocationId(person.getId(), location.getId())) {
                var pla = new PersonLikesAssociation(person, location);
                plaRepository.save(pla);
                return true;
            }
        }
        return false;
    }

    public boolean removeLikeFromLocation(String locationId, String personUsername) {
        Person person = personService.getPersonFromUsername(personUsername);
        if (person == null) return false;

        try {
            plaRepository.deleteByPersonIdAndLocationId(person.getId(), locationId);
            return true;
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
        return false;
    }

    // TODO: Implement pagination
    public List<Location> getLikedLocations(String username) throws UsernameNotFoundException {
        Person person = personService.getPersonFromUsername(username);
        if (person == null) throw new UsernameNotFoundException("Username " + username + " was not found.");

        return person.getLikedLocations(new SortPersonLikesAssociationByLikeDate());
    }

    public Page<Location> searchRegionForLocations(RegionModel model) {
        double latitudeMin = model.getLatitude() - model.getLatitudeDelta();
        double latitudeMax = model.getLatitude() + model.getLatitudeDelta();
        double longitudeMin = model.getLongitude() - model.getLongitudeDelta();
        double longitudeMax = model.getLongitude() + model.getLongitudeDelta();

        // Limit to 15 results per search
        return locationRepository.findAllByLatitudeBetweenAndLongitudeBetweenOrderByLikeCount(
                latitudeMin, latitudeMax, longitudeMin, longitudeMax, PageRequest.of(0, 15));
    }

    public boolean hasUserLikedLocation(String locationId, String personUsername) {
        Person person = personService.getPersonFromUsername(personUsername);
        if (person == null) return false;

        Optional<Location> locationOptional = locationRepository.findFirstById(locationId);

        return locationOptional.filter(location ->
                plaRepository.existsByPersonIdAndLocationId(person.getId(), location.getId())).isPresent();
    }
}
