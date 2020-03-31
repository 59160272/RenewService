package com.example.renewseviceqq;

import java.util.Date;

public class Comments {

    private String message, user_id,userPhoto,userName;
    private Date timestamp;

    public Comments(){

    }

    public Comments(String message, String user_id, Date timestamp ,String userPhoto ,String userName) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.userPhoto = userPhoto;
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
