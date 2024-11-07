package com.example.productprmapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productprmapp.models.LoginModel;
import com.example.productprmapp.models.LoginResponseModel;
import com.example.productprmapp.models.UserModel;
import com.example.productprmapp.repo.Repository;
import com.example.productprmapp.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;

    private AuthService authService;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authService = Repository.getAuthService();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signupText);

        loginButton.setOnClickListener(v -> loginUser());
        signUpTextView.setOnClickListener(v -> navigateToSignUp());
    }

    private void loginUser() {
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        LoginModel loginModel = new LoginModel(email, password);
        Call<LoginResponseModel> call = authService.login(loginModel);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.code() == 400) {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseModel loginResponseModel = response.body();
                    UserModel userModel = loginResponseModel.getUser();
                    saveSession(userModel);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                    Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSession(UserModel userModel) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", userModel.getEmail());
        editor.putString("username", userModel.getUserName());
        editor.putInt("userId", userModel.getUserId());
        editor.putString("role", userModel.getRole());
        editor.apply();
    }

    private void navigateToSignUp() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}