package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


import com.example.projectakhir_bobotek.databinding.ActivityHomeBinding;
import com.example.projectakhir_bobotek.databinding.ActivityUploadProfileImageBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ivProfile=findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(view -> {
            Intent intent=new Intent(this,ProfileActivity.class);
            startActivity(intent);

        });
    }
}