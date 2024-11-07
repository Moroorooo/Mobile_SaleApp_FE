package com.example.productprmapp.services;

import com.example.productprmapp.models.CategoryModel;
import com.example.productprmapp.models.ProductListModel;
import com.example.productprmapp.models.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    String PRODUCTS = "products";
    String PRODUCT_BY_ID = "products/{id}";
    String FILTER = "products/filters";
    String CATEGORY = "categories";


    @GET(FILTER)
        // Update with your actual search endpoint
    Call<ProductListModel> filterProduct(@Query("name") String name,
                                         @Query("sortBy") String sortBy,
                                         @Query("minPrice") double minPrice,
                                         @Query("maxPrice") double maxPrice,
                                         @Query("page") int page,
                                         @Query("price") int price);

    @GET(FILTER)
        // Update with your actual search endpoint
    Call<ProductListModel> searchProduct(@Query("name") String name);

    @GET(FILTER)
        // Update with your actual search endpoint
    Call<ProductListModel> searchProductAndSort(@Query("name") String name,
                                                @Query("sortBy") String sortBy);

    @GET(FILTER)
        // Update with your actual search endpoint
    Call<ProductListModel> searchProductAndSortAndCate(@Query("name") String name,
                                                       @Query("sortBy") String sortBy,
                                                       @Query("categoryId") String categoryId);

    @GET(FILTER)
        // Update with your actual search endpoint
    Call<ProductListModel> searchProductAndSortAndPrice(@Query("name") String name,
                                                        @Query("sortBy") String sortBy,
                                                        @Query("minPrice") double minPrice,
                                                        @Query("maxPrice") double maxPrice);

    @GET(FILTER)
        // Update with your actual search endpoint
    Call<ProductListModel> searchProductAndSortAndPriceAndCate(@Query("name") String name,
                                                               @Query("sortBy") String sortBy,
                                                               @Query("minPrice") double minPrice,
                                                               @Query("maxPrice") double maxPrice,
                                                               @Query("categoryId") String categoryId);


    @GET(FILTER)
    Call<ProductListModel> getAllProduct();

    @GET(PRODUCT_BY_ID)
    Call<ProductModel> getProductById(@Path("id") Object id);

    @GET(CATEGORY)
    Call<List<CategoryModel>> getAllCategories();
}
