package com.example.renewseviceqq;

import java.util.Date;

public class ShowPost {
    public String user_id, desc, topic, budget, address,category,postid,image,phone,userName;
    public Date timestamp;

    public ShowPost() {

    }

    public ShowPost(String user_id, String postid, String desc, String topic, String budget,
                    String address, Date timestamp,String category,String image,String phone,String userName) {
        this.user_id = user_id;
        this.postid = postid;
        this.desc = desc;
        this.topic = topic;
        this.budget = budget;
        this.address = address;
        this.category = category;
        this.timestamp = timestamp;
        this.phone = phone;
        this.userName = userName;
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


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
