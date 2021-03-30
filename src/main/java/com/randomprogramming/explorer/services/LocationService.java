package com.randomprogramming.explorer.services;

import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.randomprogramming.explorer.entities.Location;
import com.randomprogramming.explorer.entities.Media;
import com.randomprogramming.explorer.entities.Person;
import com.randomprogramming.explorer.models.LocationModel;
import com.randomprogramming.explorer.models.RegionModel;
import com.randomprogramming.explorer.repositories.LocationRepository;
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
    final private LocationRepository locationRepository;

    final private PersonService personService;

    public LocationService(LocationRepository locationRepository, PersonService personService) {
        this.locationRepository = locationRepository;
        this.personService = personService;
    }

    public boolean addLocation(LocationModel model, String username) throws IOException {
        if (model.getMedia().length == 0) {
            // Location must have at least one picture
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

        var location = new Location(model.getLatitude(), model.getLongitude(), model.getTitle(), mediaSet, person);

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
        Page<Location> locations = locationRepository.findAllByTitleLike(searchQuery, PageRequest.of(0, 15));
        return locations;
    }

    public boolean markLocationAsLiked(String locationId, String personUsername) {
        Person person = personService.getPersonFromUsername(personUsername);
        if (person == null) return false;

        Optional<Location> location = locationRepository.findFirstById(locationId);

        if (location.isPresent()) {
            // Since we're using a Set, we don't have to check if user already liked the location
            Set<Location> likedLocations = person.getLikedLocations();
            likedLocations.add(location.get());
            person.setLikedLocations(likedLocations);

            return personService.save(person);
        } else {
            return false;
        }
    }

    // TODO: Implement pagination
    public Set<Location> getLikedLocations(String username) throws UsernameNotFoundException {
        Person person = personService.getPersonFromUsername(username);
        if (person == null) throw new UsernameNotFoundException("Username " + username + " was not found.");

        return person.getLikedLocations();
    }

    public Page<Location> searchRegionForLocations(RegionModel model) {
        double latitudeMin = model.getLatitude() - model.getLatitudeDelta();
        double latitudeMax = model.getLatitude() + model.getLatitudeDelta();
        double longitudeMin = model.getLongitude() - model.getLongitudeDelta();
        double longitudeMax = model.getLongitude() + model.getLongitudeDelta();

//        TODO: Maybe remove limit or make it smaller/larger
        return locationRepository.findAllByLatitudeBetweenAndLongitudeBetweenOrderByLikeCount(
                latitudeMin, latitudeMax, longitudeMin, longitudeMax, PageRequest.of(0, 15));
    }
}
