package com.example.larkmessage.entity;

import com.example.larkmessage.unit.DateUnit;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Moment {
    private String text;
    private String image;
    private String path;
    private String userName;
    private Integer icon;
    private Date time;
    private ArrayList<String> agree;
    private ArrayList<String> disagree;
    private ArrayList<String> userList;
    public void setDefault() throws ParseException {
        agree=new ArrayList<String>();
        disagree=new ArrayList<String>();
        time = DateUnit.convertStringToDateAddTime(DateUnit.getSystemTimeAndDate());
        path =null;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ArrayList<String> getAgree() {
        return agree;
    }

    public void setAgree(ArrayList<String> agree) {
        this.agree = agree;
    }

    public ArrayList<String> getDisagree() {
        return disagree;
    }

    public void setDisagree(ArrayList<String> disagree) {
        this.disagree = disagree;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }
}
