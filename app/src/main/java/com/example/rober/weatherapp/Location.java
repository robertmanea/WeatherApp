package com.example.rober.weatherapp;

/**
 * Created by rober on 03-Apr-17.
 */

public class Location {

    private String locationName;
    private String locationCountry;
    private int locationID;

    public Location(String locationName, String locationCountry, int locationID){
        this.locationID=locationID;
        this.locationName=locationName;
        this.locationCountry=locationCountry;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCountry() { return locationCountry; }

    public void setLocationCountry(String locationCountry) { this.locationCountry = locationCountry; }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}
