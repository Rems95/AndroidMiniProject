package com.example.myapplication;

import com.google.type.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {
    List<LatLng> locations=new ArrayList<LatLng>();

    public Route(List<LatLng> locations) {
        this.locations = locations;
    }

    public List<LatLng> getLocations() {
        return locations;
    }

    public void setLocations(List<LatLng> locations) {
        this.locations = locations;
    }
}
