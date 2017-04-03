package com.example.rober.weatherapp;

/**
 * Created by rober on 03-Apr-17.
 */

public class Location {

    private String locationName;
    private int locationID;

    public Location(String locationName, int locationID){
        this.locationID=locationID;
        this.locationName=locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}
