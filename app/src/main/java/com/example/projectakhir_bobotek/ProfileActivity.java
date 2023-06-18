package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectakhir_bobotek.databinding.ActivityMainBinding;
import com.example.projectakhir_bobotek.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityProfileBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.profileBtDownload.setOnClickListener(this);
        binding.profileBtUpload.setOnClickListener(this);
        binding.profileBtLogout.setOnClickListener(this);
        binding.profileBtSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profileBtDownload:
                break;
            case R.id.profileBtUpload:
                break;
            case R.id.profileBtLogout:
                signOut();
                break;
            case R.id.profileBtSave:
                break;
        }
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}