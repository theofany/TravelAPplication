package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    CircleImageView profileP;
    TextView username,email,nphone;
    Button btn_edt_profile,btn_back;
    User user;
    ImageView img;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        getProfile();
        btn_edt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, LandingPage.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        profileP = findViewById(R.id.profile_picture);
        btn_edt_profile = findViewById(R.id.edit_profile);
        username = findViewById(R.id.username_data);
        email = findViewById(R.id.email_data);
        nphone = findViewById(R.id.nphone_data);
        img = findViewById(R.id.img_profile);
        btn_back = findViewById(R.id.back);
        reference = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid());
    }
    private void getProfile(){
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                user = task.getResult().getValue(User.class);
                if (user.getImg() != null){
                    Glide.with(Profile.this).load(user.getImg()).into(profileP);
                    profileP.setVisibility(View.VISIBLE);
                    img.setVisibility(View.INVISIBLE);
                }
                username.setText(user.getName());
                email.setText((user.getEmail()));
                nphone.setText(user.getNphone());
            }
        });
    }
}