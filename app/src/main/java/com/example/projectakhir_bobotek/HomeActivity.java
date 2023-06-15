package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projectakhir_bobotek.databinding.ActivityHomeBinding;
import com.example.projectakhir_bobotek.databinding.ActivityUploadProfileImageBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}