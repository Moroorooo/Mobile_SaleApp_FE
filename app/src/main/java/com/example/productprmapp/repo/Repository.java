package com.example.productprmapp.repo;

import com.example.productprmapp.api.APIClient;
import com.example.productprmapp.services.AuthService;
import com.example.productprmapp.services.ProductService;
import com.example.productprmapp.services.UserService;

public class Repository {
    public static AuthService getAuthService(){
        return APIClient.getClient().create(AuthService.class);
    }

    public static ProductService getProductService(){
        return APIClient.getClient().create(ProductService.class);
    }

    public static UserService getUserService(){
        return APIClient.getClient().create(UserService.class);
    }
}
