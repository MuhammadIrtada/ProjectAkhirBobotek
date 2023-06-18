package com.example.projectakhir_bobotek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.projectakhir_bobotek.databinding.ActivityProfileBinding;
import com.example.projectakhir_bobotek.databinding.ActivityRegisterBinding;
import com.example.projectakhir_bobotek.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                String email, password, fullName, phoneNumber;
                email = binding.registerEtEmail.getText().toString();
                password = binding.registerEtPassword.getText().toString();
                fullName = binding.registerEtFullName.getText().toString();
                phoneNumber = binding.registerEtPhoneNumber.getText().toString();
                registerProses(email, password, fullName, phoneNumber);
                break;
        }
    }

    private void registerProses(String email, String password, String fullName, String phoneNumber){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addDataToDatabase(mAuth.getUid(), fullName, phoneNumber);
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void addDataToDatabase(String userId, String fullName, String phoneNumber) {
        User user = new User(fullName, phoneNumber);
        mDatabase.child("users").child(userId).child("profile").setValue(user);
    }

    private void updateUI(){
        Intent intent = new Intent(this, UploadProfileImageActivity.class);
        startActivity(intent);
    }
}