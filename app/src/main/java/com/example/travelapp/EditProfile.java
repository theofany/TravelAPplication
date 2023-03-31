package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    private Button submit;
    private EditText username, nphone;
    private CircleImageView img;
    private ImageView image_profile;
    private User user;
    private String image_old;
    private Uri image_url;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private StorageReference fileRef;
    private TextView change_photo, email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        getData();
    }

    private void init() {
        progressBar = findViewById(R.id.rolling);
        img = findViewById(R.id.profile_picture_edt);
        submit = findViewById(R.id.submit);
        username = findViewById(R.id.username_edt);
        email = findViewById(R.id.email_edt);
        nphone = findViewById(R.id.nphone_edt);
        image_profile = findViewById(R.id.img_add_profile_edt);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid());
        fileRef = FirebaseStorage.getInstance().getReference().child("user").child(user1.getEmail()).child("images");
        change_photo = findViewById(R.id.change_photo);
        change_photo.setOnClickListener(this);
        submit.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
    }

    private void getData() {
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                user = task.getResult().getValue(User.class);
                if (user.getImg() != null) {
                    Glide.with(EditProfile.this).load(user.getImg()).into(img);
                    img.setVisibility(View.VISIBLE);
                    image_profile.setVisibility(View.INVISIBLE);
                }
                image_old = user.getImg();
                username.setText(user.getName());
                email.setText((user.getEmail()));
                nphone.setText(user.getNphone());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_photo:
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, 101);
                break;
            case R.id.submit:
                progressBar.setVisibility(View.VISIBLE);
                Upload();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            image_url = data.getData();
            img.setImageURI(image_url);
            img.setVisibility(View.VISIBLE);
            image_profile.setVisibility(View.INVISIBLE);
        }
    }

//    private void ProgressBar(){
//        ProgressBar progressBar = new ProgressBar(this);
//        progressBar.se
//    }

    private void Upload(){
        if (image_url == null){
            User user = new User(email.getText().toString(), username.getText().toString(), nphone.getText().toString());
            user.setImg(image_old);
            reference.setValue(user).addOnSuccessListener(succes ->{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Update berhasil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }).addOnFailureListener(failure ->{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Update gagal", Toast.LENGTH_SHORT).show();
            });
        }else{
            UploadToFirebase(image_url, username.getText().toString(), email.getText().toString(), nphone.getText().toString());
        }
    }

    private void UploadToFirebase(Uri uri, String username, String email, String nophone) {
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar.setVisibility(View.GONE);
                        User user = new User(email, username, nophone);
                        user.setImg(uri.toString());
                        reference.setValue(user);
                        Toast.makeText(getApplicationContext(), "Upload succes", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}