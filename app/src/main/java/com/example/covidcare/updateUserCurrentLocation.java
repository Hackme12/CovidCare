package com.example.covidcare;

public class updateUserCurrentLocation {

    public double latitude;
    public double longitude;

    public updateUserCurrentLocation() {
        latitude = 0.0;
        longitude = 0.0;
    }

    public updateUserCurrentLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void updateUserLocationToDatabase(){


    }





}
