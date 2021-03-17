package com.randomprogramming.explorer.services;

import com.randomprogramming.explorer.entities.Location;
import com.randomprogramming.explorer.entities.Media;
import com.randomprogramming.explorer.models.LocationModel;
import com.randomprogramming.explorer.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class LocationService {
    final private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public boolean addLocation(LocationModel model) {
        // TODO: Implement actual media upload functionality
        Set<Media> mediaSet = new HashSet<>() {{
            add(new Media("https://i.imgur.com/mCvpnRc.png"));
        }};

        var location = new Location(model.getLatitude(), model.getLongitude(), model.getTitle(), mediaSet);

        try {
            locationRepository.save(location);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
