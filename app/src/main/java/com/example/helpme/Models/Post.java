package com.example.helpme.Models;

import android.util.Log;

import com.example.helpme.Activities.PostActivity;
import com.example.helpme.Extras.Constants;

import java.io.File;

public class Post {

    private PostActivity postActivity;

    private String userId;

    private String postDescription;

    private File photo;

    private String latlong;


    public Post(PostActivity postActivity) {

        this.postActivity = postActivity;

        this.userId = null;
        this.postDescription = null;
        this.photo = null;
        this.latlong = null;
    }

    public String getPostDescription() { return postDescription; }

    public File getPhoto() { return photo; }

    public String getLatlong() { return latlong; }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
        Log.d(Constants.DB_LOG, "setPostDescription: description set");
    }

    public void setPhoto(File photo) {
        this.photo = photo;
        Log.d(Constants.DB_LOG, "setPhoto: photo file set");
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;

        Log.d(Constants.DB_LOG, "setLatlong: latlong set");
    }


}
