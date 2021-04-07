package com.example.covidcare;

public class LatLang {
    private double latitude;
    private double longitude;


    private String Uid;

    public LatLang() {
    }

    public LatLang(double latitude, double longitude, String uid) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.Uid = uid;
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
    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }








}
