package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projectakhir_bobotek.databinding.ActivityCartBinding;
import com.example.projectakhir_bobotek.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference cartReference;
    private ArrayList<Cart> cartArrayList;
    CartAdapter cartAdapter;
    private int subTotal, totalBiaya, deliverFee;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Melakukan inflate binding
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Auth
        mAuth = FirebaseAuth.getInstance();

        // Menghubungkan pada realtime database
        databaseReference = FirebaseDatabase.getInstance("https://project-akhir-bobotek-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        cartReference = this.databaseReference.child("users");

        // Mengatur layout manager
        LinearLayoutManager mLayout = new LinearLayoutManager(this);
        binding.rvCart.setLayoutManager(mLayout);

        // Menampilkan daftar cart dan mengatur biaya
        getAllCart();

        // Ketika menekan tomobol pesan
        binding.btnPay.setOnClickListener(v -> {
            pay();
        });
    }

    private void getAllCart(){
        this.cartReference.child(mAuth.getUid()).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subTotal = 0; totalBiaya = 0; deliverFee = 0;

                cartArrayList = new ArrayList<>();

                for(DataSnapshot s: snapshot.getChildren()){
                    Cart c = s.getValue(Cart.class);
                    c.setKey(s.getKey());
                    cartArrayList.add(c);

                    // Mengatur biaya
                    subTotal += c.getHarga() * c.getKuantitas();
                }
                cartAdapter = new CartAdapter(cartArrayList);
                binding.rvCart.setAdapter(cartAdapter);

                // Mengatur sub total, deliver Free, Total Biaya
                binding.tvSubTotal.setText(String.valueOf(subTotal));
                deliverFee = 12000;
                binding.tvDeliv.setText(String.valueOf(deliverFee));
                totalBiaya = subTotal + deliverFee;
                binding.tvTotalBiaya.setText(String.valueOf(totalBiaya));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Gagal menampilkan daftar cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void pay() {
        cartReference.child(mAuth.getUid()).child("profile").get().addOnCompleteListener(unused -> {
            user = new User();
            user = unused.getResult().getValue(User.class);
            if (totalBiaya > user.saldo){
                Toast.makeText(CartActivity.this, "Harap lakukan top UP terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }
            int newSaldo = user.saldo - totalBiaya;
            cartReference.child(mAuth.getUid()).child("profile").child("saldo").setValue(newSaldo);
            cartReference.child(mAuth.getUid()).child("cart").removeValue();

            // Melakukan intent
            startIntent();
        });
    }

    private void startIntent() {
        // Mengirimkan intent pada payment succes
        String reffNum = reffNum();
        String payTime = payTime();

        Intent intent = new Intent(CartActivity.this, PaymentSuccessAvticity.class);
        intent.putExtra("AMOUNT", totalBiaya);
        intent.putExtra("PAYTIME", payTime);
        intent.putExtra("REFFNUM", reffNum);
        intent.putExtra("SENDER", user.fullName);
        startActivity(intent);
    }

    // Membuat pay time
    private String payTime() {
        // Mendapatkan waktu saat ini
        LocalDateTime currentTime = LocalDateTime.now();

        // Membuat objek DateTimeFormatter dengan format yang diinginkan
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, HH:mm:ss");

        // Mengubah waktu menjadi string dengan format yang telah ditentukan
        return currentTime.format(formatter);
    }

    // MEmbuat reff number
    private String reffNum() {
        // Mendapatkan waktu saat ini
        LocalDateTime currentTime = LocalDateTime.now();

        // Membuat objek DateTimeFormatter dengan format yang diinginkan
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmssddMMyyyy");

        // Mengubah waktu menjadi string dengan format yang telah ditentukan
        return currentTime.format(formatter);
    }
}
