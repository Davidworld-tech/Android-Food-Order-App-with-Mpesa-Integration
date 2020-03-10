package com.example.foodz;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodz.Interface.ItemClickListener;

public class EnterpriseAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtHotelName, txtHotelLocation;
    public ImageView HotelLogo;
    public ItemClickListener listener;

    public EnterpriseAdapter(@NonNull View itemView) {
        super(itemView);
        txtHotelName=itemView.findViewById(R.id.HotelName);
        txtHotelLocation=itemView.findViewById(R.id.location);
        HotelLogo=itemView.findViewById(R.id.HotelLogo);
    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener =listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);

    }
}

