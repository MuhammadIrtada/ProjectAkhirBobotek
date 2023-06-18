package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectakhir_bobotek.databinding.ActivityCartBinding;
import com.example.projectakhir_bobotek.databinding.ActivityPaymentBinding;

public class PaymentSuccessAvticity extends AppCompatActivity {
    ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Melakukan inflate binding
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        binding.psTvAmount.setText(intent.getStringExtra("AMOUNT"));
        binding.psTvSender.setText(intent.getStringExtra("SENDER"));
        binding.psTvPayMethod.setText("BoBoTek Wallet");
        binding.psTvRevNum.setText(intent.getStringExtra("REFFNUM"));
        binding.psTvMerName.setText("BoBoTek");
        binding.psTvPayTime.setText(intent.getStringExtra("PAYTIME"));

        binding.psBtnToHome.setOnClickListener(v -> {
            Intent i = new Intent(PaymentSuccessAvticity.this, HomeActivity.class);
            startActivity(i);
        });
    }
}