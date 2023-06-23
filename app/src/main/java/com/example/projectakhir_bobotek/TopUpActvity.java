package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    DatabaseReference profileReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //mengambil intent
        Intent intent = getIntent();
        this.amount = intent.getIntExtra("AMPROFILE", 0);

        // instansiasi database dan auth
        FirebaseSingleton firebaseSingleton = FirebaseSingleton.getInstance();
        mAuth = firebaseSingleton.getFirebaseAuth();
        profileReference = firebaseSingleton.getFirebaseDatabase().child("users").child(mAuth.getUid()).child("profile");

        binding.tuBtnTopUp.setOnClickListener(v -> topUp());

        binding.tuBtBack.setOnClickListener(v -> {
            finish();
        });
    }

    // topUp -> melakukan update pada saldo profile sesuai isi editText
    private void topUp() {
        if (!validateForm()){
            return;
        }
        int newAmount = amount + Integer.parseInt(binding.tuEtAmount.getText().toString());
        String phone = binding.tuEtPhone.getText().toString();
        profileReference.child("saldo").setValue(newAmount).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Berhasil Membahkan saldo", Toast.LENGTH_SHORT).show();
        });
        finish();
    }

    // User tidak bisa melakukan topUp ketika data edit text kosong
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.tuEtPhone.getText().toString())){
            binding.tuEtPhone.setError("Required");
            result = false;
        } else {
            binding.tuEtPhone.setError(null);
        }
        if (TextUtils.isEmpty(binding.tuEtAmount.getText().toString())){
            binding.tuEtAmount.setError("Required");
            result = false;
        } else {
            binding.tuEtAmount.setError(null);
        }
        return result;
    }
}