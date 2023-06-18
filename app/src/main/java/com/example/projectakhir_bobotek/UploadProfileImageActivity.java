package com.example.projectakhir_bobotek;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.projectakhir_bobotek.databinding.ActivityRegisterBinding;
import com.example.projectakhir_bobotek.databinding.ActivityUploadProfileImageBinding;
import com.example.projectakhir_bobotek.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class UploadProfileImageActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUploadProfileImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadProfileImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.uploadProfileImageImgUpload.setOnClickListener(this);
        binding.uploadProfileImageBtNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.uploadProfileImageImgUpload:
                selectImage();
                break;
            case R.id.uploadProfileImageBtNext:
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        // Get the data from an ImageView as bytes
        binding.uploadProfileImageUserImage.setDrawingCacheEnabled(true);
        binding.uploadProfileImageUserImage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.uploadProfileImageUserImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
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
                                }else {
                                    Toast.makeText(UploadProfileImageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(UploadProfileImageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UploadProfileImageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }


                Intent intent = new Intent(UploadProfileImageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadProfileImageActivity.this);
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
                    binding.uploadProfileImageUserImage.post(() -> {
                       binding.uploadProfileImageUserImage.setImageBitmap(bitmap);
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
                binding.uploadProfileImageUserImage.post(() -> {
                    binding.uploadProfileImageUserImage.setImageBitmap(bitmap);
                });
            });
            thread.start();
        }
    }

    private void addDataToDatabase(String profileImage) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(mAuth.getUid()).child("profile").child("profileImage").setValue(profileImage);

        mAuth.signOut();
    }
}