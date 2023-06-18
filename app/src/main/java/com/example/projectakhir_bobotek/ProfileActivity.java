package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.projectakhir_bobotek.databinding.ActivityMainBinding;
import com.example.projectakhir_bobotek.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.profileBtDownload.setOnClickListener(this);
        binding.profileBtUpload.setOnClickListener(this);
        binding.profileBtSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profileBtDownload:
                break;
            case R.id.profileBtUpload:
                break;
            case R.id.profileBtSave:
                break;
        }
    }
}