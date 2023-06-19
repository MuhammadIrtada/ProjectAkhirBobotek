package com.example.projectakhir_bobotek;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectakhir_bobotek.databinding.ActivityDetailBinding;
import com.example.projectakhir_bobotek.model.Medicine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class ProductDetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private Medicine medicine;
    DatabaseReference userReference;
    FirebaseAuth mAuth;

    // Mengecek name dan variabel
    boolean isSameName = false;
    int getKuantitas;
    String getKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Melakukan inflate binding
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menghubungkan pada realtime database
        FirebaseSingleton firebaseSingleton = FirebaseSingleton.getInstance();
        userReference = firebaseSingleton.getFirebaseDatabase().child("users");
        mAuth = firebaseSingleton.getFirebaseAuth();

        // mengambil intent
        medicine = getIntent().getParcelableExtra("EXTRA MEDICINE");

        binding.pdTvName.setText(medicine.getNama());
        binding.pdTvKategori.setText(medicine.getKategori());
        binding.pdTvDes.setText(medicine.getDeskripsi());
        binding.pdTvHarga.setText("Rp." + String.valueOf(medicine.getHarga()));

        binding.pdBtnPesan.setOnClickListener(v -> {
            pesanMedicine();
        });

        binding.back.setOnClickListener(v -> {
            finish();
        });
    }

    private void pesanMedicine(){
        Cart c = new Cart(medicine.getNama(), medicine.getHarga(), 1, medicine.getStok());
        userReference.child(mAuth.getUid()).child("cart").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot s: task.getResult().getChildren()){
                    Cart c = s.getValue(Cart.class);
                    c.setKey(s.getKey());
                    if (Objects.equals(medicine.getNama(), c.getNama())){
                        isSameName = true;
                        getKuantitas = c.getKuantitas();
                        getKey = c.getKey();
                    }
                }
                System.out.println(isSameName);
                if (isSameName) {
                    userReference.child(mAuth.getUid()).child("cart").child(getKey).child("kuantitas").setValue(getKuantitas+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ProductDetailActivity.this, "Cart sudah ada", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    userReference.child(mAuth.getUid()).child("cart").push().setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });
    }
}