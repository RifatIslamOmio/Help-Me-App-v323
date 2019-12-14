package com.example.helpme.Models;

import com.example.helpme.Activities.PostActivity;

public class Post {

    private PostActivity postActivity;

    private String postDescription;

    private Photo photo;

    private String latlong;


    public Post(PostActivity postActivity) {
        this.postActivity = postActivity;
    }

    public String getPostDescription() { return postDescription; }

    public Photo getPhoto() { return photo; }

    public String getLatlong() { return latlong; }

    public void setPostDescription(String postDescription) { this.postDescription = postDescription; }

    public void setPhoto(Photo photo) { this.photo = photo; }

    public void setLatlong(String latlong) { this.latlong = latlong; }


}
