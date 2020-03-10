package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodz.Adapters.AdminOrderViewHolder;
import com.example.foodz.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {


    private RecyclerView OrdersRecView;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);
        getSupportActionBar().hide();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        OrdersRecView = findViewById(R.id.OrderRecList);
        OrdersRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(ordersRef, Orders.class)
                        .build();
        FirebaseRecyclerAdapter<Orders, AdminOrderViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, AdminOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final Orders model) {
                holder.username.setText("Name " + model.getName());
                holder.userPhoneNumber.setText("Phone " + model.getPhoneNumber());
                holder.userTotalAmount.setText("Total Amount " + model.getTotalAmount());
                holder.UserAddress.setText("Address " + model.getAddress());
                holder.orderTime.setText("Order @" + model.getDate() + model.getTime());
                holder.showOrders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProducts.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{
                                "yes",
                                "No"
                        };

                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("Have you shipped this order products?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    String uID = getRef(position).getKey();
                                    removeOrder(uID);

                                }else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new AdminOrderViewHolder(view);
            }
        };

        OrdersRecView.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String uID) {
        ordersRef.child(uID).removeValue();

    }
}
