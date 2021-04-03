package com.randomprogramming.explorer.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LocationModel {
    private double latitude;
    private double longitude;
    private String title;
    private String description;
    private MultipartFile[] media;

    public boolean hasNullValues() {
        return title == null || media == null;
    }
}
