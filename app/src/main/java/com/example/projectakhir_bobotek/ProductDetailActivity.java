package com.example.projectakhir_bobotek;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectakhir_bobotek.databinding.ActivityProductDetailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private Medicine medicine;

    DatabaseReference databaseReference;
    DatabaseReference cartReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Melakukan inflate binding
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menghubungkan pada realtime database
        databaseReference = FirebaseDatabase.getInstance("https://project-akhir-bobotek-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        cartReference = databaseReference.child("user");

        // Auth
        mAuth = FirebaseAuth.getInstance();

        // mengambil intent
        medicine = getIntent().getParcelableExtra("EXTRA MEDICINE");

        binding.pdTvName.setText(medicine.getNama());
        binding.pdTvKategori.setText(medicine.getKategori());
        binding.pdTvDes.setText(medicine.getDeskripsi());
        binding.pdTvHarga.setText("Rp." + String.valueOf(medicine.getHarga()));

        binding.pdBtnPesan.setOnClickListener(v -> {
            pesanMedicine();
        });
    }

    private void pesanMedicine(){
        Cart c = new Cart(medicine.getNama(), medicine.getHarga(), 1, medicine.getStok());
        cartReference.child(mAuth.getUid()).child("cart").push().setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProductDetailActivity.this, "Medicine ditambahkan pada cart", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailActivity.this, "Gagal menambahkan pada cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}