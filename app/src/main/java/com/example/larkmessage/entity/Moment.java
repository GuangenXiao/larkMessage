package com.example.larkmessage.entity;

import java.util.ArrayList;

public class Moment {
    private String text;
    private String image;
    private String time;
    private Integer agree;
    private Integer disagree;
    private ArrayList<String> comment;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getAgree() {
        return agree;
    }

    public void setAgree(Integer agree) {
        this.agree = agree;
    }

    public Integer getDisagree() {
        return disagree;
    }

    public void setDisagree(Integer disagree) {
        this.disagree = disagree;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    public void setComment(ArrayList<String> comment) {
        this.comment = comment;
    }
}
