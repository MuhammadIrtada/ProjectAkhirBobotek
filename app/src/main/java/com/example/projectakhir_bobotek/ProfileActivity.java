package com.example.projectakhir_bobotek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.projectakhir_bobotek.databinding.ActivityMainBinding;
import com.example.projectakhir_bobotek.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;

    // Inisiasi database refrence
    DatabaseReference databaseReference;
    DatabaseReference profileReference;

    FirebaseAuth mAuth;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menginstansiasi Auth
        mAuth = FirebaseAuth.getInstance();

        // Menginstansiasi database
        databaseReference = FirebaseDatabase.getInstance("https://project-akhir-bobotek-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        profileReference = databaseReference.child("users").child(mAuth.getUid()).child("profile");

        // Mengambil data profile
        getProfile();

        // button Top Up
        user = new User();
        binding.profileBtTopUp.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, TopUpActvity.class);
            intent.putExtra("AMPROFILE", user.saldo);
            startActivity(intent);
        });

        // button Save
        binding.profileBtSave.setOnClickListener(v ->
                saveProfile());
    }

    public void getProfile() {
        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                binding.profileEtFullName.setText(user.fullName);
                binding.profileEtPhone.setText(user.phoneNumber);
                binding.profileTvAmount.setText(String.valueOf(user.saldo));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }

    public void saveProfile() {
        String newName = binding.profileEtFullName.getText().toString();
        String newPhone = binding.profileEtPhone.getText().toString();
        user.fullName = newName;
        user.phoneNumber = newPhone;
        profileReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProfileActivity.this, "Berhasil melakukan save", Toast.LENGTH_SHORT).show();
            }
        });
    }
}