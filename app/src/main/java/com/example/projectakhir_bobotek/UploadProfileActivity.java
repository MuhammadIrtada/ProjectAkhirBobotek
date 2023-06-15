package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class UploadProfileActivity extends AppCompatActivity {

    ImageButton uploadProfileImageBtNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);

        uploadProfileImageBtNext=findViewById(R.id.uploadProfileImageBtNext);
    }
}