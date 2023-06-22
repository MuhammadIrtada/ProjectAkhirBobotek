package com.example.projectakhir_bobotek.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String fullName;
    public String phoneNumber;
    public String profileImage;
    public String address;
    public int saldo;
    public double latitude;
    public double longitude;

    public User(){}

    public User(String fullName, String phoneNumber){
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.saldo = 0;
        this.profileImage = "/profile.png";
    }
}
