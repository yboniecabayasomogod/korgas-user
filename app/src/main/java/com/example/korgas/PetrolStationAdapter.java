package com.example.korgas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PetrolStationAdapter extends RecyclerView.Adapter<PetrolStationAdapter.PetrolStationViewHolder> {

    Context context;

    ArrayList<ReadPetrolStationData> list;

    public PetrolStationAdapter(Context context, ArrayList<ReadPetrolStationData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PetrolStationAdapter.PetrolStationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent, false);
        return  new PetrolStationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PetrolStationViewHolder holder, int position) {

        ReadPetrolStationData readPetrolStationData = list.get(position);

        holder.petrolStationName.setText(readPetrolStationData.getName());
        holder.gasolinePrice.setText(readPetrolStationData.getGasolinePrice());
        holder.dieselPrice.setText(readPetrolStationData.getDieselPrice());
        holder.kerosenePrice.setText(readPetrolStationData.getKerosenePrice());
        holder.address.setText(readPetrolStationData.getAddress());

        Picasso.get().load(readPetrolStationData.getPetrolStationPicture()).into(holder.imageView);

        holder.webLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(readPetrolStationData.getWebsiteLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        holder.mapLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(readPetrolStationData.getMapLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    public static class PetrolStationViewHolder extends RecyclerView.ViewHolder {

        TextView petrolStationName;
        TextView gasolinePrice;
        TextView dieselPrice;
        TextView kerosenePrice;
        TextView address;
        MaterialButton webLink, mapLink;
        ImageView imageView;

        public PetrolStationViewHolder(@NonNull View itemView) {
            super(itemView);

            petrolStationName = itemView.findViewById(R.id.petrolStationName);
            gasolinePrice = itemView.findViewById(R.id.textViewGasolinePrice);
            dieselPrice = itemView.findViewById(R.id.textViewDieselPrice);
            kerosenePrice = itemView.findViewById(R.id.textViewKerosenePrice);
            address = itemView.findViewById(R.id.address);
            imageView = itemView.findViewById(R.id.petrolStationIMG);
            webLink = itemView.findViewById(R.id.idBtnWebLink);
            mapLink = itemView.findViewById(R.id.idBtnMapLink);
            imageView = itemView.findViewById(R.id.petrolStationIMG);
        }
    }
}
