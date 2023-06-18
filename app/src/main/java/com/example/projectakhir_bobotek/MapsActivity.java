package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.android.gms.maps.MapView;

public class MapsActivity extends AppCompatActivity {

    ImageButton ibMaps;
    SearchView svMaps;
    Button btMaps;
    MapView mvMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ibMaps = findViewById(R.id.ibBackMaps);
        svMaps = findViewById(R.id.svMaps);
        btMaps = findViewById(R.id.btSaveMaps);
        mvMaps = findViewById(R.id.mvMaps);

        ibMaps.setOnClickListener(view -> {
            Intent intent = new Intent();
            startActivity(intent);
        });
    }
}