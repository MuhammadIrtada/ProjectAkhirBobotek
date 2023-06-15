package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtRegister=findViewById(R.id.registerBtRegister);
        registerBtRegister.setOnClickListener(view -> {
            Intent intent=new Intent(RegisterActivity.this,UploadProfileActivity.class);
            startActivity(intent);
        });

    }
}