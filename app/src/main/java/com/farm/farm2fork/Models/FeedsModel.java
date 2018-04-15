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
    private String like_status;

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

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
