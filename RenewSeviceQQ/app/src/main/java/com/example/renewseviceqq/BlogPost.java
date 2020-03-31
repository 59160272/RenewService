package com.example.renewseviceqq;

import android.widget.Spinner;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class BlogPost{

    public String user_id, image, desc, topic, budget, address, userPhoto, userName,category,postid,phone;
    public Date timestamp;
    //public Spinner category;

    public BlogPost() {}

    public BlogPost(String user_id, String image, String desc, String topic, String budget,
                    String address, Date timestamp, String userPhoto, String userName,String category,String postid,String phone) {
        this.user_id = user_id;
        this.image = image;
        this.desc = desc;
        this.topic = topic;
        this.budget = budget;
        this.address = address;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.category = category;
        this.timestamp = timestamp;
        this.postid = postid;
        this.phone = phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String Address) {
        this.address = Address;
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
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
