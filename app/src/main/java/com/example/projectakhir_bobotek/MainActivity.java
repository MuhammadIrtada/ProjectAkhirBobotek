package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGo=findViewById(R.id.imgPanah);
        btnGo.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        });
    }
}