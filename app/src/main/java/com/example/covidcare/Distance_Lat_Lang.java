package com.example.covidcare;

public class Distance_Lat_Lang {

    public double latitude_1, longitude_1;
    public double latitude_2, longitude_2;

    public Distance_Lat_Lang() {

    }
    public Distance_Lat_Lang(double lat_1, double long_1, double lat_2,double long_2) {
        this.latitude_1 = lat_1;
        this.longitude_1 = long_1;
        this.latitude_2 = lat_2;
        this.longitude_2 = long_2;
    }



    public void setLatitude1(double latitude) {
       this.latitude_1 = latitude;
    }
    public void setLongitude1(double longitude) {
        this.longitude_1 = longitude;
    }
    public void setLatitude2(double latitude_2) {
        this.latitude_2 = latitude_2;
    }
    public void setLongitude2(double longitude_2) {
        this.longitude_2 = longitude_2;
    }

    public double getLatitude_1() {
        return latitude_1;
    }

    public double getLatitude_2() {
        return latitude_2;
    }

    public double getLongitude_1() {
        return longitude_1;
    }

    public double getLongitude_2() {
        return longitude_2;
    }

    public  double distance_Between_LatLong() {

        this.latitude_1 = Math.toRadians(this.latitude_1);
        this.latitude_2 = Math.toRadians(this.latitude_2);
        this.longitude_1 = Math.toRadians(this.longitude_1);
        this.longitude_2 = Math.toRadians(this.longitude_2);

        double earthRadius = 6371.01 * 1000; //meters
        return earthRadius * Math.acos(Math.sin(latitude_1)*Math.sin(latitude_2) + Math.cos(latitude_1)*Math.cos(latitude_2)*Math.cos(longitude_1 - longitude_2));
    }

    public boolean check_if_in_exposed_area(){
        if(distance_Between_LatLong()<=1.8288){
           return true;
        }
        else{
            return false;
        }
    }
}






