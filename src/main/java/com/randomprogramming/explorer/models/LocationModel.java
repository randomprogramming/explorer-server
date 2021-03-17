package com.randomprogramming.explorer.models;

import lombok.Data;

@Data
public class LocationModel {
    private double latitude;
    private double longitude;
    private String title;

    public boolean hasNullValues() {
        return title == null;
    }
}
