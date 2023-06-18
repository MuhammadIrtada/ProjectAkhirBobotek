package com.example.projectakhir_bobotek.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Medicine implements Parcelable {
    private String nama;
    private String kategori;
    private String deskripsi;
    private int harga;
    private int stok;
    private String key;

    // Constructor
    public Medicine(String nama, String kategori, String deskripsi, int harga, int stok){
        this.nama = nama;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.stok = stok;
    }

    Medicine() {

    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getHarga() {
        return harga;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getStok() {
        return stok;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.key);
        parcel.writeString(this.nama);
        parcel.writeString(this.kategori);
        parcel.writeString(this.deskripsi);
        parcel.writeInt(this.stok);
        parcel.writeInt(this.harga);
    }
    private Medicine(Parcel in){
        this.key = in.readString();
        this.nama = in.readString();
        this.kategori = in.readString();
        this.deskripsi = in.readString();
        this.stok = in.readInt();
        this.harga = in.readInt();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>(){

        @Override
        public Medicine createFromParcel(Parcel parcel) {
            return new Medicine(parcel);
        }

        @Override
        public Medicine[] newArray(int i) {
            return new Medicine[i];
        }
    };
}
