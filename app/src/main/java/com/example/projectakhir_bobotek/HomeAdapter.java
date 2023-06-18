package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectakhir_bobotek.databinding.ItemMadicineBinding;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.VH> {
    // Membuat array list Medicine
    private final ArrayList<Medicine> medicineArrayList;

    // Membuat constructor
    public HomeAdapter(ArrayList<Medicine> medicines) {
        this.medicineArrayList = medicines;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMadicineBinding binding = ItemMadicineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(medicineArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return medicineArrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        // Melakukan binding pada view holder
        final ItemMadicineBinding binding;

        public VH(ItemMadicineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Medicine medicine){
            binding.homeTvNamaObat.setText(medicine.getNama());
            binding.homeTvKategori.setText(medicine.getKategori());
            binding.homeTvDes.setText(medicine.getDeskripsi());
            binding.homeTvHarga.setText(String.valueOf(medicine.getHarga()));
            binding.homeCvMedicine.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                intent.putExtra("EXTRA MEDICINE", medicine);
                v.getContext().startActivity(intent);
            });
        }
    }
}
