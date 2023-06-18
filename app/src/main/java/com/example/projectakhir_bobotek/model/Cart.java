package com.example.projectakhir_bobotek.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Cart {
    public String nama;
    public int harga;
    public int kuantitas;

    public Cart(){}

    public Cart(String nama, int harga, int kuantitas) {
        this.nama = nama;
        this.harga = harga;
        this.kuantitas = kuantitas;
    }
}
