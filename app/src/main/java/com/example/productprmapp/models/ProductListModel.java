package com.example.productprmapp.models;

import java.util.List;

public class ProductListModel {
    private int totalElements;
    private int totalPages;
    private List<ProductModel> products;

    public ProductListModel() {
    }

    public ProductListModel(int totalElements, int totalPages, List<ProductModel> products) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.products = products;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
