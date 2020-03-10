package com.example.foodz.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodz.Interface.ItemClickListener;
import com.example.foodz.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtProductName, txtProductPrice, txtProductQuantity, txtTotal;
    private ItemClickListener itemClickListener;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.product_d_name);
        txtProductPrice=itemView.findViewById(R.id.product_d_price);
        txtProductQuantity=itemView.findViewById(R.id.product_d_quantity);
        txtTotal=itemView.findViewById(R.id.product_d_total);
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
