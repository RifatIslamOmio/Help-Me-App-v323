package com.example.helpme.Models;

import android.util.Log;

import com.example.helpme.Extras.Constants;
import com.example.helpme.everything.Comment;
import java.util.ArrayList;
import java.util.List;


//For sending data
public class Help {
    private String helpId;
    private String seeker_name;
    private String description;
    private String dateandtime;
    private boolean handled = false;
    private String user_id;
    private String latlong;
    private String current_address;
    private String photo_path= "https://i.imgur.com/FzjpMaM.png";
    private int voteCount;
    private List<String> voters = null;
    private List<Comment> commentList;
    private int commentCount;


    public Help() {
        this.voteCount = 0;
        voters = new ArrayList<>();
        //voters.add("null");
        commentList = new ArrayList<>();
        this.commentCount = 0;
    }

    public Help(String helpId, String seeker_name, String description, String dateandtime,String user_id, String latlong, String current_address, String photo_path) {
        this.helpId = helpId;
        this.seeker_name = seeker_name;
        this.description = description;
        this.dateandtime = dateandtime;
        this.user_id = user_id;
        this.latlong = latlong;
        this.current_address = current_address;
        this.photo_path = photo_path;
        this.voteCount = 0;
        this.commentCount = 0;
        //voters.add("null");
        commentList = new ArrayList<>();
    }



    public String getHelpId() {
        return helpId;
    }

    public void setHelpId(String helpId) {
        Log.d(Constants.DB_LOG, "setHelpId: setting helpId");
        this.helpId = helpId;
    }

    public String getSeeker_name() {
        return seeker_name;
    }

    public void setSeeker_name(String seeker_name) {
        Log.d(Constants.DB_LOG, "setHelpId: setting seeker_name");
        this.seeker_name = seeker_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Log.d(Constants.DB_LOG, "setHelpId: setting description");
        this.description = description;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        Log.d(Constants.DB_LOG, "setHelpId: setting dateandtime");
        this.dateandtime = dateandtime;
    }


    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        Log.d(Constants.DB_LOG, "setHelpId: setting handled bool");
        this.handled = handled;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        Log.d(Constants.DB_LOG, "setHelpId: setting setUser_id");
        this.user_id = user_id;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        Log.d(Constants.DB_LOG, "setHelpId: setting latlong");
        this.latlong = latlong;
    }

    public String getCurrent_address() {
        return current_address;
    }

    public void setCurrent_address(String current_address) {
        Log.d(Constants.DB_LOG, "setHelpId: setting current_address");
        this.current_address = current_address;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        Log.d(Constants.DB_LOG, "setHelpId: setting photo_path");
        this.photo_path = photo_path;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        Log.d(Constants.DB_LOG, "setHelpId: setting voteCount");
        this.voteCount = voteCount;
    }

    public List<String> getVoters() {
        return voters;
    }

    public void setVoters(List<String> voters) {
        Log.d(Constants.DB_LOG, "setHelpId: setting voters");
        this.voters = voters;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
