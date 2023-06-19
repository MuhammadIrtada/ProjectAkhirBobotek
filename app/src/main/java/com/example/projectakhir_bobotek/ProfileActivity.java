package com.example.projectakhir_bobotek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.projectakhir_bobotek.databinding.ActivityProfileBinding;
import com.example.projectakhir_bobotek.model.User;
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
    DatabaseReference profileReference;

    FirebaseAuth mAuth;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menghubungkan database dan auth
        FirebaseSingleton firebaseSingleton = FirebaseSingleton.getInstance();
        mAuth = firebaseSingleton.getFirebaseAuth();
        profileReference = firebaseSingleton.getFirebaseDatabase().child("users").child(mAuth.getUid()).child("profile");

        // Mengambil data profile
        getProfile();

        // button Top Up
        user = new User();
        binding.profilBtTopUp.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, TopUpActvity.class);
            intent.putExtra("AMPROFILE", user.saldo);
            startActivity(intent);
        });

        // button Save
        binding.profileBtSave.setOnClickListener(v ->
                saveProfile());

        binding.back.setOnClickListener(v -> {
            finish();
        });
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
        if (!validateForm()){
            return;
        }

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

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.profileEtFullName.getText().toString())){
            binding.profileEtFullName.setError("Required");
            result = false;
        } else {
            binding.profileEtFullName.setError(null);
        }
        if (TextUtils.isEmpty(binding.profileEtPhone.getText().toString())){
            binding.profileEtPhone.setError("Required");
            result = false;
        } else {
            binding.profileEtPhone.setError(null);
        }
        return result;
    }
}