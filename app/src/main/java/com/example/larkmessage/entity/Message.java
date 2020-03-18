package com.example.larkmessage.entity;

public class Message {

    String ID;
    String username;
    String conetxt;
    String time;

    public Message(String ID, String username, String conetxt, String time) {
        this.ID = ID;
        this.username = username;
        this.conetxt = conetxt;
        this.time = time;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConetxt() {
        return conetxt;
    }

    public void setConetxt(String conetxt) {
        this.conetxt = conetxt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
