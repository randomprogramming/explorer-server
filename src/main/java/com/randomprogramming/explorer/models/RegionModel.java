package com.randomprogramming.explorer.models;

import lombok.Data;

@Data
// The provided Region spans from (latitude-latitudeDelta) to (latitude+latitudeDelta) and same for longitude
public class RegionModel {
    private double latitude;
    private double latitudeDelta;
    private double longitude;
    private double longitudeDelta;
}
