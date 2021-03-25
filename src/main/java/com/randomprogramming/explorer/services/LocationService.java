package com.randomprogramming.explorer.services;

import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.randomprogramming.explorer.entities.Location;
import com.randomprogramming.explorer.entities.Media;
import com.randomprogramming.explorer.models.LocationModel;
import com.randomprogramming.explorer.repositories.LocationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
            mediaSet.add(new Media(url));
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
}
