package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.example.projectakhir_bobotek.databinding.ActivityProfileBinding;
import com.example.projectakhir_bobotek.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerBtRegister.setOnClickListener(this);
        binding.registerBtRegister.setEnabled(false);

        binding.registerCbPrivacyPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Mengubah status button sesuai dengan status checkbox
                binding.registerBtRegister.setEnabled(isChecked);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerBtRegister:
                registerProses();
                break;
        }
    }

    private void registerProses(){
        Intent intent = new Intent(this, UploadProfileImageActivity.class);
        startActivity(intent);
    }
}