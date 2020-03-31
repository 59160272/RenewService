package com.example.renewseviceqq;

public class Users {
    public String user_id,userPhone,userName,userPhoto,postid;

    public Users()  {
    }
    public Users(String user_id,String userName,String userPhone,String userPhoto,String postid) {
        this.user_id = user_id;
        this.userName = userName;
        //this.userPhone = userPhone;
        this.userPhoto = userPhoto;
        this.postid = postid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

   /* public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }*/

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
