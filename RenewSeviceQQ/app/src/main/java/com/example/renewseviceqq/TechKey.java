package com.example.renewseviceqq;


public class TechKey extends IDtech {
    public String techUserId, techTitle, techSpinner, techImage, techDesc, techAddress, techPhone,techKeyword;

    public TechKey()  {
    }
    public TechKey(String techUserId, String techTitle, String techSpinner, String techImage, String techDesc,String techPhone,
                    String techAddress,String techKeyword ) {
        this.techUserId = techUserId;
        this.techTitle = techTitle;
        this.techSpinner = techSpinner;
        this.techImage = techImage;
        this.techDesc = techDesc;
        this.techAddress = techAddress;
        this.techPhone = techPhone;
        this.techKeyword = techKeyword;

    }

    public String getTechUserId() {
        return techUserId;
    }

    public String getTechTitle() {
        return techTitle;
    }

    public String getTechSpinner() {
        return techSpinner;
    }

    public String getTechImage() {
        return techImage;
    }

    public String getTechDesc() {
        return techDesc;
    }

    public String getTechAddress() {
        return techAddress;
    }

    public void setTechUserId(String techUserId) {
        this.techUserId = techUserId;
    }

    public void setTechTitle(String techTitle) {
        this.techTitle = techTitle;
    }

    public void setTechSpinner(String techSpinner) {
        this.techSpinner = techSpinner;
    }

    public void setTechImage(String techImage) {
        this.techImage = techImage;
    }

    public void setTechDesc(String techDesc) {
        this.techDesc = techDesc;
    }

    public void setTechAddress(String techAddress) {
        this.techAddress = techAddress;
    }

    public String getTechPhone() {
        return techPhone;
    }

    public void setTechPhone(String techPhone) {
        this.techPhone = techPhone;
    }

    public String getTechKeyword() {
        return techKeyword;
    }

    public void setTechKeyword(String techKeyword) {
        this.techKeyword = techKeyword;
    }


}
