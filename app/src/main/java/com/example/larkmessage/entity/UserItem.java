package com.example.larkmessage.entity;

import android.os.Build;

public class UserItem{
    private String userName=null;
    private String userId=null;
    private String password=null;
    private Integer textSize=null;
    private Integer textStyle=null;
    private Integer bgColor=null;
    private String phoneNumber=null;
    private String email=null;
    private String time=null;
    private final   String FriendListKey="friendList";
    private final   String MomentListKey="MomentList";
    public UserItem(String userName, String userId,String password, Integer textSize, Integer textStyle, Integer bgColor, String phoneNumber, String email, String time) {
        this.password =password;
        this.userName = userName;
        this.userId = userId;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.bgColor = bgColor;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.time = time;
    }
    public String getFriendListKey() {
        return FriendListKey;
    }

    public String getMomentListKey() {
        return MomentListKey;
    }

    public static class Builder
    {
        private String password=null;
        private String userName=null;
        private String userId=null;
        private Integer textSize=null;
        private Integer textStyle=null;
        private Integer bgColor=null;
        private String phoneNumber=null;
        private String email=null;
        private String time=null;
        public Builder(String userName, String email)
        {
            this.userName =userName;
            this.email =email;
        }
        public Builder userId(String userId)
        {
            this.userId=userId;
            return this;
        }
        public Builder textSize(Integer textSize)
        {
            this.textSize=textSize;
            return this;
        }
        public Builder password(String password)
        {
            this.password=password;
            return this;
        }
        public  Builder textStyle()
        {
            this.textStyle =textStyle;
            return this;
        }
        public Builder bgColor(Integer bgColor)
        {
            this.bgColor =bgColor;
            return this;
        }
        public  Builder phoneNumber(String phoneNumber)
        {
            this.phoneNumber =phoneNumber;
            return  this;
        }
        public  Builder time(String time)
        {
            this.time =time;
            return this;

        }
        public UserItem Build()
        {
            return new UserItem( userName,  userId,password,  textSize,  textStyle,  bgColor,  phoneNumber, email, time);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTextSize() {
        return textSize;
    }

    public void setTextSize(Integer textSize) {
        this.textSize = textSize;
    }

    public Integer getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(Integer textStyle) {
        this.textStyle = textStyle;
    }

    public Integer getBgColor() {
        return bgColor;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBgColor(Integer bgColor) {
        this.bgColor = bgColor;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}