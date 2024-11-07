package com.example.productprmapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.productprmapp.R;
import com.example.productprmapp.adapter.ImagePagerAdapter;
import com.example.productprmapp.adapter.ProductAdapter;
import com.example.productprmapp.models.CategoryModel;
import com.example.productprmapp.models.ProductListModel;
import com.example.productprmapp.models.ProductModel;
import com.example.productprmapp.models.QueryModel;
import com.example.productprmapp.repo.Repository;
import com.example.productprmapp.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomePageFragment extends Fragment {

    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler();
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<ProductModel> productList = new ArrayList<>();

    private String currentSearchQuery = "";

    private List<CategoryModel> categoryList = new ArrayList<>();
    private Spinner categorySpinner;
    private ProductService productService;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        productService = Repository.getProductService();

        viewPager = view.findViewById(R.id.viewPager);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.banner1);
        imageList.add(R.drawable.banner3);
        imageList.add(R.drawable.banner2);

        EditText minPriceEditText = view.findViewById(R.id.minPriceEditText);
        EditText maxPriceEditText = view.findViewById(R.id.maxPriceEditText);
        Button filterBtn = view.findViewById(R.id.filterBtn);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(getContext(), imageList);
        viewPager.setAdapter(pagerAdapter);

        Spinner spinner = view.findViewById(R.id.sortSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.sort_array,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setQueryHint("Bạn muốn mua gì hôm nay...");  // Ensure hint is set in code

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        fetchCategories();
        // Fetch products from API
        fetchProducts(null);

        // Start the auto-scrolling
        startAutoScroll();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getContext(), "Searching for " + query, Toast.LENGTH_SHORT).show();

                minPriceEditText.setText("");
                maxPriceEditText.setText("");
                spinner.setSelection(0);
                categorySpinner.setSelection(0);
                currentSearchQuery = query;
                QueryModel queryModel = new QueryModel();
                queryModel.setName(query);
                queryModel.setSortBy("popularity");
                queryModel.setMinPrice(Double.valueOf(-2));
                queryModel.setMaxPrice(Double.valueOf(-1));
                queryModel.setCategoryId("0");
                fetchProducts(queryModel); // Fetch products based on the search query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery().length() == 0) {
                    minPriceEditText.setText("");
                    maxPriceEditText.setText("");
                    spinner.setSelection(0);
                    fetchProducts(null);
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String minPriceStr = minPriceEditText.getText().toString();
                String maxPriceStr = maxPriceEditText.getText().toString();

                String selectedItem = parent.getItemAtPosition(position).toString();
                QueryModel queryModel = new QueryModel();
                queryModel.setName(currentSearchQuery);
                queryModel.setSortBy(selectedItem);
                queryModel.setMinPrice(minPriceStr.isEmpty() ? Double.valueOf(-2) : Double.valueOf(minPriceStr));
                queryModel.setMaxPrice(maxPriceStr.isEmpty() ? Double.valueOf(-1) : Double.valueOf(maxPriceStr));
                queryModel.setCategoryId(String.valueOf(categorySpinner.getTag()));
                fetchProducts(queryModel); // Fetch products based on the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle the case when nothing is selected
            }
        });

        filterBtn.setOnClickListener(v -> {
            String minPriceStr = minPriceEditText.getText().toString();
            String maxPriceStr = maxPriceEditText.getText().toString();

            // Parse prices
            Double minPrice = minPriceStr.isEmpty() ? Double.valueOf(-2) : Double.valueOf(minPriceStr);
            Double maxPrice = maxPriceStr.isEmpty() ? Double.valueOf(-1) : Double.valueOf(maxPriceStr);

            if (minPrice < 0 || maxPrice < 0) {
                Toast.makeText(getContext(), "Please input valid price", Toast.LENGTH_SHORT).show();
                return; // Exit early if validation fails
            }

            if (minPrice != -2 && maxPrice != -1 && minPrice > maxPrice) {

                Toast.makeText(getContext(), "Min Price cannot be greater than Max Price", Toast.LENGTH_SHORT).show();
                return; // Exit early if validation fails
            }

            // Parse prices and create a new QueryModel
            QueryModel queryModel = new QueryModel();
            queryModel.setMinPrice(minPriceStr.isEmpty() ? Double.valueOf(-2) : Double.valueOf(minPriceStr));
            queryModel.setMaxPrice(maxPriceStr.isEmpty() ? Double.valueOf(-1) : Double.valueOf(maxPriceStr));
            queryModel.setName(currentSearchQuery);
            queryModel.setSortBy(spinner.getSelectedItem().toString());
            queryModel.setCategoryId(String.valueOf(categorySpinner.getTag()));
            fetchProducts(queryModel);
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String minPriceStr = minPriceEditText.getText().toString();
                String maxPriceStr = maxPriceEditText.getText().toString();

                String selectedCategoryName = parent.getItemAtPosition(position).toString();

                int selectedCategoryId = categoryList.stream()
                        .filter(category -> category.getCategoryName().equals(selectedCategoryName))
                        .findFirst()
                        .map(CategoryModel::getCategoryId) // Use map to get the category ID
                        .orElse(0);
                QueryModel queryModel = new QueryModel();
                queryModel.setCategoryId(String.valueOf(selectedCategoryId));
                queryModel.setMinPrice(minPriceStr.isEmpty() ? Double.valueOf(-2) : Double.valueOf(minPriceStr));
                queryModel.setMaxPrice(maxPriceStr.isEmpty() ? Double.valueOf(-1) : Double.valueOf(maxPriceStr));
                queryModel.setName(currentSearchQuery);
                queryModel.setSortBy(spinner.getSelectedItem().toString());
                Toast.makeText(getContext(), "Selected category: " + selectedCategoryId, Toast.LENGTH_SHORT).show();
                fetchProducts(queryModel); // Fetch products based on the selected category
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected if necessary
            }
        });


        return view;
    }

    private void startAutoScroll() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % 3;  // Assuming 3 images
                viewPager.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 4000); // Auto-scroll every 4 seconds
            }
        };
        sliderHandler.postDelayed(runnable, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacksAndMessages(null);
    }

    private void fetchProducts(QueryModel query) {
        Call<ProductListModel> call;
        if (query != null) {
            if (query.getMinPrice() == -2) {
                if ("0".equals(query.getCategoryId())) {
                    call = productService.searchProductAndSort(query.getName(), query.getSortBy());
                } else {
                    call = productService.searchProductAndSortAndCate(query.getName(), query.getSortBy(), query.getCategoryId());
                }
            } else {
                if ("0".equals(query.getCategoryId())) {
                    call = productService.searchProductAndSortAndPrice(query.getName(), query.getSortBy(), query.getMinPrice(), query.getMaxPrice());
                } else {
                    call = productService.searchProductAndSortAndPriceAndCate(query.getName(), query.getSortBy(), query.getMinPrice(), query.getMaxPrice(), query.getCategoryId());
                }
            }
        } else {
            call = productService.getAllProduct();
        }

        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear(); // Clear previous results
                    for (ProductModel product : response.body().getProducts()) {
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {
                // Handle failure

            }
        });
    }

    private void fetchCategories() {
        productService.getAllCategories().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    populateCategorySpinner();
                } else {
                    Toast.makeText(getContext(), "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("All Categories");
        for (CategoryModel category : categoryList) {
            categoryNames.add(category.getCategoryName()); // Adjust if your model is different
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }


}

