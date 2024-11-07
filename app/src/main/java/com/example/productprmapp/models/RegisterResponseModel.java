package com.example.productprmapp.models;

public class RegisterResponseModel {
    private String message;
    private UserModel user;

    public RegisterResponseModel() {
    }

    public RegisterResponseModel(String message, UserModel user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
