package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectakhir_bobotek.databinding.ActivityTopUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TopUpActvity extends AppCompatActivity {
    ActivityTopUpBinding binding;

    int amount;
    // inisiasi database
    DatabaseReference databaseReference;
    DatabaseReference profilReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //mengambil intent
        Intent intent = getIntent();
        this.amount = intent.getIntExtra("AMPROFILE", 0);

        // instansiasi mAuth
        mAuth = FirebaseAuth.getInstance();

        // instansiasi database
        databaseReference = FirebaseDatabase.getInstance("https://project-akhir-bobotek-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        profilReference = databaseReference.child("users").child(mAuth.getUid()).child("profile");

        binding.tuBtnTopUp.setOnClickListener(v -> topUp());
    }
    private void topUp() {
        int newAmount = amount + Integer.parseInt(binding.tuEtAmount.getText().toString());
        String phone = binding.tuEtPhone.getText().toString();
        profilReference.child("saldo").setValue(newAmount).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Berhasil Membahkan saldo", Toast.LENGTH_SHORT).show();
        });
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}