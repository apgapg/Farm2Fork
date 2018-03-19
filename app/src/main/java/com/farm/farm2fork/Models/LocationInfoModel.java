package com.farm.farm2fork.Models;

/**
 * Created by master on 24/2/17.
 */

public class LocationInfoModel {

    private String latitude;
    private String longitude;
    private String address;
    private String postalzipcode;
    private String city;

    public String getAddress() {
        return address;
    }


    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPostalzipcode() {
        return postalzipcode;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPostalzipcode(String postalzipcode) {
        this.postalzipcode = postalzipcode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
