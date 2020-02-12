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

    public Comment(){}

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

    public String commentFormat()
    {
        if(this.commentText.length()<40)
        {
            String[] strara = this.commentText.split(" ");
            String temp ="";
            for(String s:strara)
            {
                if(s.equals("snatcher")||s.equals("Snatcher")||s.equals("attacker"))
                {
                    temp=temp+"<font color='#FD2929'>"+ s+"</font>"+" ";
                }
                else if(s.equals("robber")||s.equals("Robber")||s.equals("robbing"))
                {
                    temp=temp+"<font color='#FD2929'>"+ s+"</font>"+" ";
                }
                else if(s.equals("danger")||s.equals("Dangerous"))
                {
                    temp=temp+"<font color='#FD2929'>"+ s+"</font>"+" ";
                }
                else if(s.equals("Harmful")||s.equals("harmful"))
                {
                    temp=temp+"<font color='#FD2929'>"+ s+"</font>"+" ";
                }
                else if(s.equals("safe")||s.equals("Safe")||s.equals("secure")||s.equals("rescued"))
                {
                    temp=temp+"<font color='#2D81FF'>"+ s+"</font>"+" ";
                }
                else if(s.equals("999")||s.equals("911")||s.equals("police")||s.equals("rab")||s.equals("RAB"))
                {
                    temp=temp+"<font color='#FF593D'>"+ s+"</font>"+" ";
                }
                else{temp=temp+s+" ";}

            }
            return temp.trim();
        }
        else
        {
            return this.commentText.trim();
        }

    }
}
