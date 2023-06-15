package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectakhir_bobotek.databinding.ActivityLoginBinding;
import com.example.projectakhir_bobotek.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBtLogin.setOnClickListener(this);
        binding.loginTvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginBtLogin:
                String email, password;
                email = binding.loginEtEmail.getText().toString();
                password = binding.loginEtPassword.getText().toString();

                loginProses(email, password);
                break;
            case R.id.loginTvRegister:
                registerProses();
                break;
        }
    }

    private void loginProses(String email, String password){
        if (email.equals("bobotek") && password.equals("bobotek")){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void registerProses(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}