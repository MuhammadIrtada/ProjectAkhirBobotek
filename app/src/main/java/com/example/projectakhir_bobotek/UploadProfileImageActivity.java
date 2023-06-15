package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectakhir_bobotek.databinding.ActivityRegisterBinding;
import com.example.projectakhir_bobotek.databinding.ActivityUploadProfileImageBinding;

public class UploadProfileImageActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUploadProfileImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadProfileImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.uploadProfileImageBtNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.uploadProfileImageBtNext:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}