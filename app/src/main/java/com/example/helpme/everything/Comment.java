package com.example.helpme.everything;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private String comment_id;
    private String cUserId;
    private String cUserName;
    private String helpPostId;
    private String date;
    private String time;
    private String commentText;
    private int upvoteCount;
    private List<String> commentVoters = null;


    public Comment() { }

    public Comment(String comment_id, String cUserId, String cUserName,String helpPostId, String date, String time, String commentText) {
        this.comment_id = comment_id;
        this.cUserId = cUserId;
        this.cUserName = cUserName;
        this.helpPostId = helpPostId;
        this.date = date;
        this.time = time;
        this.commentText = commentText;
        this.upvoteCount=0;
        commentVoters = new ArrayList<>();
        commentVoters.add("null");
    }


    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getcUserId() {
        return cUserId;
    }

    public void setcUserId(String cUserId) {
        this.cUserId = cUserId;
    }

    public String getcUserName() {
        return cUserName;
    }

    public void setcUserName(String cUserName) {
        this.cUserName = cUserName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public List<String> getCommentVoters() {
        return commentVoters;
    }

    public void setCommentVoters(List<String> commentVoters) {
        this.commentVoters = commentVoters;
    }

    public String getHelpPostId() {
        return helpPostId;
    }

    public void setHelpPostId(String helpPostId) {
        this.helpPostId = helpPostId;
    }
}
