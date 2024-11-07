package com.example.productprmapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.productprmapp.models.UserModel;
import com.example.productprmapp.repo.Repository;
import com.example.productprmapp.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private UserService userService;
    private ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_detail);

        userService = Repository.getUserService();

        usernameEditText = findViewById(R.id.edtUserName);
        emailEditText = findViewById(R.id.edtEmail);
        phoneEditText = findViewById(R.id.edtPhone);
        addressEditText = findViewById(R.id.edtAddress);
        backBtn = findViewById(R.id.btnBack);

        backBtn.setOnClickListener(v -> finish());
        SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
        Integer userId = sharedPreferences.getInt("userId", 0);
        loadUserDetail(userId);
    }

    private void loadUserDetail(Integer userId) {
        Call<UserModel> call = userService.getUserById(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userModel = response.body();
                if (userModel != null) {
                    usernameEditText.setText(userModel.getUserName());
                    emailEditText.setText(userModel.getEmail());
                    phoneEditText.setText(userModel.getPhoneNumber());
                    addressEditText.setText(userModel.getAddress());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }
}