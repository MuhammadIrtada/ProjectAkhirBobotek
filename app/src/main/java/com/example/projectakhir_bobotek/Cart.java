package com.example.projectakhir_bobotek;

public class Cart {
    private String key;
    private String nama;
    private int harga;
    private int kuantitas;
    private int stok;
    private int source;

    Cart () {

    }

    Cart (String nama, int harga, int kuantitas, int stok, int source){
        this.nama = nama;
        this.harga = harga;
        this.kuantitas = kuantitas;
        this.stok = stok;
        this.source = source;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getStok() {
        return stok;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getSource() {
        return source;
    }
}
