package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TourDetail extends AppCompatActivity {
    TextView txtnama, txtdesc;
    ImageView img;
    String id;
    ModelRV modelRV;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        txtnama=findViewById(R.id.name_tour_dtl);
        txtdesc=findViewById(R.id.desc_tour_dtl);
        img=findViewById(R.id.img_tour_dtl);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        reference = FirebaseDatabase.getInstance().getReference().child("tour");

        reference.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    modelRV = task.getResult().getValue(ModelRV.class);
                    Glide.with(TourDetail.this).load(modelRV.getImg()).into(img);
                    txtnama.setText(modelRV.getNama());
                    txtdesc.setText(modelRV.getDesc());
                }
            }
        });
    }
}