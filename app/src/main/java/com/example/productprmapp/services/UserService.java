package com.example.productprmapp.services;

import com.example.productprmapp.models.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    String USER_BY_ID = "users/{id}";

    @GET(USER_BY_ID)
    Call<UserModel> getUserById (@Path("id") Integer id);
}
