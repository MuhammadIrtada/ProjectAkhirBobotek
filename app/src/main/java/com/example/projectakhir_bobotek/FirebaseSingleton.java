package com.example.projectakhir_bobotek;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.Serializable;

public class FirebaseSingleton implements Serializable {
    private static FirebaseSingleton instance;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    private FirebaseSingleton (){
        try {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance("https://project-akhir-bobotek-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static synchronized FirebaseSingleton getInstance(){
        if (instance == null){
            instance = new FirebaseSingleton();
        }
        return instance;
    }

    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }

    public DatabaseReference getFirebaseDatabase() {
        return firebaseDatabase;
    }
}
