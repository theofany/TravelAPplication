package com.example.travelapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class AdapterRV extends RecyclerView.Adapter<AdapterRV.MyViewHolder>{
    private android.content.Context context;
    private ArrayList<ModelRV> dataTour;


    public AdapterRV(Context cont, ArrayList<ModelRV> data) {
        context = cont;
        dataTour = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_dashboard_adapter, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.vnama.setText(dataTour.get(position).getNama());
        Glide.with(context).load(dataTour.get(position).getImg()).into(holder.vimg);
        holder.vcard.setOnClickListener(view -> {
            Intent intent = new Intent(context,TourDetail.class);
            intent.putExtra("id", dataTour.get(position).getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataTour.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView vnama;
        ImageView vimg;
        CardView vcard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            vnama = itemView.findViewById(R.id.name_tour);
            vimg = itemView.findViewById(R.id.img_tour);
            vcard = itemView.findViewById(R.id.cv_adapter);
        }
    }
}
