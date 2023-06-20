package com.example.projectakhir_bobotek;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectakhir_bobotek.databinding.ItemProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
    private final ArrayList<Cart> cartArrayList; // Membuat array list Medicine
    public CartAdapter(ArrayList<Cart> carts) {
        this.cartArrayList = carts;
    } // Membuat constructor

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(cartArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        // Melakukan binding pada view holder
        final ItemProductBinding binding;

        // Menginisiasi database reference & firebase Auth
        DatabaseReference databaseReference;
        DatabaseReference cartReference;
        FirebaseAuth mAuth;

        public VH(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Instansias authentikasi & realtime database
           FirebaseSingleton firebaseSingleton = FirebaseSingleton.getInstance();
           mAuth = firebaseSingleton.getFirebaseAuth();
           cartReference = firebaseSingleton.getFirebaseDatabase().child("users").child(mAuth.getUid()).child("cart");
        }

        public void bind(Cart cart){
            binding.cartTvNamaObat.setText(cart.getNama());
            binding.cartTvHarga.setText(String.valueOf(cart.getHarga()));
            binding.cartTvKuantitas.setText(String.valueOf(cart.getKuantitas()));
            binding.cartImg.setImageResource(cart.getSource());
            binding.cartIcPlus.setOnClickListener(v -> {
                int tambah = cart.getKuantitas() + 1;
                this.cartReference.child(cart.getKey()).child("kuantitas").setValue(tambah)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                cart.setKuantitas(tambah);
                                Toast.makeText(v.getContext(), "melakukan penambahan" , Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Gagal melakukan penambahan" , Toast.LENGTH_SHORT).show();
                            }
                        });
            });
            binding.cartIcMin.setOnClickListener(v -> {
                if (cart.getKuantitas() == 1){
                    return; // tidak bisa melakukan pengurangan
                }
                int kurang = cart.getKuantitas() - 1;
                this.cartReference.child(cart.getKey()).child("kuantitas").setValue(kurang)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                cart.setKuantitas(kurang);
                                Toast.makeText(v.getContext(), "melakukan pengurangan" , Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Gagal melakukan pengurangan" , Toast.LENGTH_SHORT).show();
                            }
                        });
            });
            binding.cartIcTrash.setOnClickListener(v -> {
                this.cartReference.child(cart.getKey()).removeValue().addOnSuccessListener(unused -> {
                    Toast.makeText(v.getContext(), "Berhasil Menghapus cart" , Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(v.getContext(), "Gagal Menghapus cart" , Toast.LENGTH_SHORT).show();
                });
            });
        }
    }
}

