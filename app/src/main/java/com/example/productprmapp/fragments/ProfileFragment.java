package com.example.productprmapp.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.productprmapp.LoginActivity;
import com.example.productprmapp.ProfileDetailActivity;
import com.example.productprmapp.R;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView;
    private Button logoutButton;
    private ImageView profileDetailImage;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("MySession", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.userNameTextView);
        logoutButton = view.findViewById(R.id.logout_btn);
        profileDetailImage = view.findViewById(R.id.imgProfileDetail);

        String username = sharedPreferences.getString("username", "");
        usernameTextView.setText(username);

        logoutButton.setOnClickListener(v -> logOut());
        profileDetailImage.setOnClickListener(v -> profileDetailActivity());
        return view;
    }

    @SuppressLint("CommitPrefEdits")
    private void logOut() {
        sharedPreferences = requireActivity().getSharedPreferences("MySession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish();

        Toast.makeText(requireContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
    }

    private void profileDetailActivity() {
        startActivity(new Intent(getActivity(), ProfileDetailActivity.class));
    }
}