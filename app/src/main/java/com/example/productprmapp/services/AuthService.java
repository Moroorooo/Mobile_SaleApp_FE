package com.example.productprmapp.services;

import com.example.productprmapp.models.LoginModel;
import com.example.productprmapp.models.LoginResponseModel;
import com.example.productprmapp.models.RegisterModel;
import com.example.productprmapp.models.RegisterResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    String LOGIN = "auths/login";
    String REGISTER = "auths/register";

    @POST(LOGIN)
    Call<LoginResponseModel> login(@Body LoginModel loginModel);

    @POST(REGISTER)
    Call<RegisterResponseModel> register(@Body RegisterModel registerModel);
}
