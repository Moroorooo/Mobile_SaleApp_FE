package com.example.productprmapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.productprmapp.models.RegisterModel;
import com.example.productprmapp.models.RegisterResponseModel;
import com.example.productprmapp.models.UserModel;
import com.example.productprmapp.repo.Repository;
import com.example.productprmapp.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, usernameEditText, passwordEditText, confirmPasswordEditText, phoneNumberEditText;
    private TextView signInTextView;
    private Button registerButton;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        authService = Repository.getAuthService();

        emailEditText = findViewById(R.id.email);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        phoneNumberEditText = findViewById(R.id.phone_number);
        registerButton = findViewById(R.id.register_button);
        signInTextView = findViewById(R.id.sign_in_txt);

        registerButton.setOnClickListener(v -> registerUser());
        signInTextView.setOnClickListener(v -> navigateToSignIn());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        // Validate email format
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return;
        }

        // Validate username
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            usernameEditText.requestFocus();
            return;
        }
        if (username.length() < 4 || username.length() > 20) {
            usernameEditText.setError("Username must be between 4 and 20 characters");
            usernameEditText.requestFocus();
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Confirm password is required");
            confirmPasswordEditText.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // Validate phone number
        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Phone number is required");
            phoneNumberEditText.requestFocus();
            return;
        }
        if (!phoneNumber.matches("^\\d{10}$")) {
            phoneNumberEditText.setError("Phone number must be 10 digits");
            phoneNumberEditText.requestFocus();
            return;
        }

        // If all validations pass, create RegisterModel and proceed with registration
        RegisterModel registerModel = new RegisterModel(
                username, password, confirmPassword, email, phoneNumber, ""
        );

        Call<RegisterResponseModel> call = authService.register(registerModel);
        call.enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponseModel registerResponseModel = response.body();
                    UserModel userModel = registerResponseModel.getUser();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();

                    Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToSignIn() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}