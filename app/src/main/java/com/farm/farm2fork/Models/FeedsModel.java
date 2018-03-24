package com.farm.farm2fork.Models;

/**
 * Created by master on 24/2/17.
 */

public class FeedsModel {

    private String uid;
    private String postid;
    private String name;
    private String city;
    private String date;
    private String image;
    private String description;
    private String like;
    private String comment;

    public String getCity() {
        return city;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getLike() {
        return like;
    }

    public String getName() {
        return name;
    }

    public String getPostid() {
        return postid;
    }
}
