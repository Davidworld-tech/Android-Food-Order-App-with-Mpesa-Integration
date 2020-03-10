package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodz.Adapters.CartViewHolder;
import com.example.foodz.model.Cart;
import com.example.foodz.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference CartListRef;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);
        recyclerView=findViewById(R.id.OrderProductList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userID=getIntent().getStringExtra("uid");
        CartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userID).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(CartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText( "Quantity= " +model.getQuantity_Ordered());
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("@  "+model.getPrice()+" Ksh");
                holder.txtTotal.setText("Total=  "+(Integer.valueOf(model.getPrice())* Integer.valueOf(model.getQuantity_Ordered())));
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
