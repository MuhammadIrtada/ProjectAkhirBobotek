package com.example.projectakhir_bobotek;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectakhir_bobotek.databinding.ActivityProfileBinding;
import com.example.projectakhir_bobotek.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;

    // Inisiasi database refrence
    DatabaseReference databaseReference;
    DatabaseReference profileReference;

    FirebaseAuth mAuth;

    User user;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menghubungkan database dan auth
        FirebaseSingleton firebaseSingleton = FirebaseSingleton.getInstance();
        mAuth = firebaseSingleton.getFirebaseAuth();
        profileReference = firebaseSingleton.getFirebaseDatabase().child("users").child(mAuth.getUid()).child("profile");
        storage = FirebaseStorage.getInstance();
        // Mengambil data profile
        getProfile();

        // button Top Up
        user = new User();
        binding.profilBtTopUp.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, TopUpActvity.class);
            intent.putExtra("AMPROFILE", user.saldo);
            startActivity(intent);
        });

        // button Save
        binding.profileBtSave.setOnClickListener(v -> {
            saveProfile();
            deleteImage();
            uploadImage();
        });

        // button upload
        binding.profileBtUpload.setOnClickListener(v -> {
            selectImage();
        });

        // button download
        binding.profileBtDownload.setOnClickListener(v -> {
            downloadImage();
        });

        // button logout
        binding.profileBtLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        binding.back.setOnClickListener(v -> {
            finish();
        });
    }

    public void getProfile() {
        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                binding.profileEtFullName.setText(user.fullName);
                binding.profileEtPhone.setText(user.phoneNumber);
                binding.profileTvAmount.setText(String.valueOf(user.saldo));
                Glide.with(getApplicationContext()).load(user.profileImage).into(binding.profileIvProfileImage);
                binding.profileEtEmail.setText(mAuth.getCurrentUser().getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }

    public void saveProfile() {
        if (!validateForm()){
            return;
        }

        String newName = binding.profileEtFullName.getText().toString();
        String newPhone = binding.profileEtPhone.getText().toString();
        user.fullName = newName;
        user.phoneNumber = newPhone;
        profileReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProfileActivity.this, "Berhasil melakukan save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.profileEtFullName.getText().toString())){
            binding.profileEtFullName.setError("Required");
            result = false;
        } else {
            binding.profileEtFullName.setError(null);
        }
        if (TextUtils.isEmpty(binding.profileEtPhone.getText().toString())){
            binding.profileEtPhone.setError("Required");
            result = false;
        } else {
            binding.profileEtPhone.setError(null);
        }
        return result;
    }

    private void selectImage() {
        binding.profileIvProfileImage.destroyDrawingCache();
        binding.profileIvProfileImage.setDrawingCacheEnabled(false);
        final CharSequence[] items = {"Take Photo", "Choose from library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Upload Image");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
            }else if (items[item].equals("Choose from library")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/+");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 20);
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            final Uri path = data.getData();
            Thread thread = new Thread(() -> {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.profileIvProfileImage.post(() -> {
                        binding.profileIvProfileImage.setImageBitmap(bitmap);
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        if (requestCode == 10 && resultCode == RESULT_OK) {
            final Bundle extras = data.getExtras();
            Thread thread = new Thread(() -> {
                Bitmap bitmap = (Bitmap) extras.get("data");
                binding.profileIvProfileImage.post(() -> {
                    binding.profileIvProfileImage.setImageBitmap(bitmap);
                });
            });
            thread.start();
        }
    }

    private void deleteImage() {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child(getPathFromUrl(user.profileImage));

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Hapus Berhasil");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                System.out.println("Hapus Gagal");
            }
        });
    }

    private void uploadImage() {
        // Get the data from an ImageView as bytes
        binding.profileIvProfileImage.setDrawingCacheEnabled(true);
        binding.profileIvProfileImage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.profileIvProfileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("images").child("IMG" + new Date().getTime() + ".jpeg");

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.getResult() != null) {
                                    addDataToDatabase(task.getResult().toString());
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void downloadImage() {
        DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(user.profileImage);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(getApplicationContext(), DIRECTORY_DOWNLOADS, getPathFromUrl(user.profileImage).split("/")[1]);

        downloadManager.enqueue(request);
    }

    private void addDataToDatabase(String profileImage) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(mAuth.getUid()).child("profile").child("profileImage").setValue(profileImage);
    }

    private String getPathFromUrl(String url) {
        String[] tempPath = url.split("/o/");
        String[] realPath = tempPath[1].split("alt=");
        return realPath[0].substring(0, realPath[0].length() - 1).replace("%2F", "/");
    }
}