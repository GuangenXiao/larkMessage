package com.example.larkmessage.entity;

import java.util.ArrayList;

public class Chat {
    String ChattingName;
    String CreateDate;
    ArrayList<String> emailList = new ArrayList<>();

    public String getChattingName() {
        return ChattingName;
    }

    public void setChattingName(String chattingName) {
        ChattingName = chattingName;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public ArrayList<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<String> emailList) {
        this.emailList = emailList;
    }
}
