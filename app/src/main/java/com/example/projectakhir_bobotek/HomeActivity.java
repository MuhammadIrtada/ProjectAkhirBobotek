package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.projectakhir_bobotek.databinding.ActivityHomeBinding;
import com.example.projectakhir_bobotek.model.Medicine;
import com.example.projectakhir_bobotek.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding; // Binding ActivityHome
    private ArrayList<Medicine> medicineArratList; // Arraylist untuk daftar medicine
    private HomeAdapter homeAdapter; // Adapter

    // inisiais DatabaseRefernce
    private DatabaseReference medicineReference;
    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Melakukan inflate binding
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menghubungkan pada Firebase
        FirebaseSingleton firebaseSingleton = FirebaseSingleton.getInstance();
        mAuth = firebaseSingleton.getFirebaseAuth();
        userReference = firebaseSingleton.getFirebaseDatabase().child("users").child(mAuth.getUid()).child("profile");
        medicineReference = firebaseSingleton.getFirebaseDatabase().child("medicine");

        // Melakuakan create medicine pada realtime database
//         binding.homeBtnAddMedicine.setOnClickListener(v -> {
//            mAuth.signOut();
//            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
//            startActivity(intent);
//        }
//         );

        // Mengatur Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.homeRvMdcn.setLayoutManager(layoutManager);

        // Menampilkan semua daftar obat
        getAllMedicine();

        // Button cart
        binding.homeIcCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Button profile
        binding.homeIcProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                updateUI(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        };
        userReference.addValueEventListener(userListener);
    }

    // mengambil seluruh data obat
    private void getAllMedicine () {
        this.medicineReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineArratList = new ArrayList<>();
                for (DataSnapshot s: snapshot.getChildren()){
                    Medicine m = s.getValue(Medicine.class);
                    m.setKey(s.getKey());
                    medicineArratList.add(m);
                }
                homeAdapter = new HomeAdapter(medicineArratList);
                binding.homeRvMdcn.setAdapter(homeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        System.out.println("backpressed");
        finishAndRemoveTask();
    }

    // Instansiai obat
    private void createMedicine() {
        Medicine obat1 = new Medicine("Betadine Antiseptic", "Antiseptic", "Antiseptic solution for disinfection.", 6100, 10);
        pushFirebase(obat1);

        Medicine obat2 = new Medicine("Panadol Biru - 500 mg", " Analgesik", "Paracetamol or acetaminophen.", 16000, 12);
        pushFirebase(obat2);

        Medicine obat3 = new Medicine("Tolak Angin - 15 ml", "Herbal Supplement", "Relieving colds, flu, and general fatigue.", 3000, 5);
        pushFirebase(obat3);

        Medicine obat4 = new Medicine("Diapet Kapsul - 10 Pcs", "Antasid", "Herbal medicine used to treat diarrhea.", 3000, 15);
        pushFirebase(obat4);
    }

    // Menambahkan obat pada firebase database
    private void pushFirebase(Medicine medicine){
        this.medicineReference.push().setValue(medicine)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(HomeActivity.this, "berhasil Menambahkan data medicine", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "Gagal menambahkan data medicine", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(User user) {
        binding.homeTvBalance.setText("Rp " + user.saldo);
        if (user.address != null) {
            binding.homeTvAddress.setText(user.address);
        }

        Glide.with(getApplicationContext()).load(user.profileImage).into(binding.homeIcProfile);
        getAllMedicine();
    }
}