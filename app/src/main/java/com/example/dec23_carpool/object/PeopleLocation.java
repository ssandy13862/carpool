package com.example.dec23_carpool.object;

public class PeopleLocation {

    String email;
    int travelInfoId;
    double driverLat;
    double driverLng;
    double lat;
    double lng;
    String result;

    public PeopleLocation(String email, double lat, double lng) {
        this.email = email;
        this.lat = lat;
        this.lng = lng;
    }

    public PeopleLocation(String email, int travelInfoId, double driverLat, double driverLng) {
        this.email = email;
        this.travelInfoId = travelInfoId;
        this.driverLat = driverLat;
        this.driverLng = driverLng;
    }

    public int getTravelInfoId() {
        return travelInfoId;
    }

    public void setTravelInfoId(int travelInfoId) {
        this.travelInfoId = travelInfoId;
    }

    public double getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(double driverLat) {
        this.driverLat = driverLat;
    }

    public double getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(double driverLng) {
        this.driverLng = driverLng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}