package com.example.productprmapp.models;

public class LoginResponseModel {
    private String message;
    private UserModel user;
    private String accessToken;

    public LoginResponseModel(String message, UserModel user, String accessToken) {
        this.message = message;
        this.user = user;
        this.accessToken = accessToken;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
