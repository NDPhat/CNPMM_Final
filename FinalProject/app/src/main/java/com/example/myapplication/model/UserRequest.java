package com.example.myapplication.model;


import com.google.gson.annotations.SerializedName;

public class UserRequest {
        @SerializedName("img_data_str")
        private String img_data_str;
        private String name;
        @SerializedName("messeage")
        private String messeage;

    public UserRequest(String imageUser, String name ) {
        this.img_data_str = imageUser;
        this.name= name;
    }

    public UserRequest(String imageUser, String name, String message) {
        this.img_data_str = imageUser;
        this.name = name;
        this.messeage = message;
    }
    public UserRequest(String imageUser) {
        this.img_data_str = imageUser;

    }

    public String getMessage() {
        return messeage;
    }

    public void setMessage(String message) {
        this.messeage = message;
    }

    public String getImageUser() {
        return img_data_str;
    }

    public void setImageUser(String imageUser) {
        this.img_data_str = imageUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

