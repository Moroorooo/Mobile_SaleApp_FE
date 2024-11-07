package com.example.productprmapp.services;

import com.example.productprmapp.models.CategoryModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    String CATEGORY = "categories";

    @GET(CATEGORY)
    Call<CategoryModel> getAllCategories();

}
