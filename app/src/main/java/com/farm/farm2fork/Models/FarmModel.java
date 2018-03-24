package com.farm.farm2fork.Models;

/**
 * Created by master on 24/2/17.
 */

public class FarmModel {

    private String crop;
    private String loc_address;
    private String loc_lat;
    private String loc_long;
    private String postalCode;
    private String farmsize;
    private String uid;
    private String loc_city;
    private String loc_key;

    public String getLoc_key() {
        return loc_key;
    }

    public String getCrop() {
        return crop;
    }

    public String getFarmsize() {
        return farmsize;
    }

    public String getLoc_address() {
        return loc_address;
    }

    public String getLoc_lat() {
        return loc_lat;
    }

    public String getLoc_long() {
        return loc_long;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getUid() {
        return uid;
    }

    public String getLoc_city() {
        return loc_city;
    }
}
