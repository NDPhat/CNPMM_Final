package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class user {
    @SerializedName("img_data_str")
    private String imageUser;

    @SerializedName("messeage")
    private String message;

    public user(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getImageUser() {
        return imageUser;
    }

    public user(String imageUser, String message) {
        this.imageUser = imageUser;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }
}
