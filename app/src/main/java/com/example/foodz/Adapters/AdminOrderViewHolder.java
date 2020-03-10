package com.example.foodz.Adapters;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodz.R;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder {

    public TextView username, userPhoneNumber, userTotalAmount, UserAddress, orderTime;
    public Button showOrders;
    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        username=itemView.findViewById(R.id.order_user_name);
        userPhoneNumber=itemView.findViewById(R.id.Order_PhoneNumber);
        userTotalAmount=itemView.findViewById(R.id.Order_t_Amount);
        UserAddress=itemView.findViewById(R.id.OrderAddress);
        orderTime=itemView.findViewById(R.id.OrderTime);
        showOrders=itemView.findViewById(R.id.showAllProductsBtn);
    }

}
