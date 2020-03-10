package com.example.foodz;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodz.Interface.ItemClickListener;

public class productsAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtProductName, txtDescription, txtPrice, txtQuantity;
    public ImageView imageView;
    public ItemClickListener listener;



    public productsAdapter(@NonNull View itemView) {
        super(itemView);

        txtQuantity=itemView.findViewById(R.id.available_display);
        imageView=itemView.findViewById(R.id.product_image);
        txtProductName=itemView.findViewById(R.id.product_name);
        txtDescription=itemView.findViewById(R.id.product_description);
        txtPrice=itemView.findViewById(R.id.product_price);


    }
    public void setItemClickListener(ItemClickListener listener){

        this.listener =listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);

    }
}
