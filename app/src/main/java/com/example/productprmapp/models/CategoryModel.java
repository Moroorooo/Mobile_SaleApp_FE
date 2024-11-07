package com.example.productprmapp.models;

public class CategoryModel {
    public int CategoryID;
    public String CategoryName;

    public CategoryModel() {
    }

    public CategoryModel(int categoryId, String categoryName) {
        this.CategoryID = categoryId;
        this.CategoryName = categoryName;
    }

    public int getCategoryId() {
        return CategoryID;
    }

    public void setCategoryId(int categoryId) {
        this.CategoryID = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.CategoryName = categoryName;
    }
}
