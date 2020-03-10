package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodz.Prevalent.Prevalent;
import com.example.foodz.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productName, productDescription, productPrice;
    private String productID = "", state="Normal";
    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //hideActionBar
        getSupportActionBar().hide();

        // addToCartBtn=findViewById(R.id.add_product_to_cart);
        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_details);
        productName = findViewById(R.id.product_name_details);
        productDescription = findViewById(R.id.product_description_details);
        productPrice = findViewById(R.id.product_Price_details);
        addToCartButton = findViewById(R.id.pd_add_to_cart);
        productID = getIntent().getStringExtra("pid");
        getProductDetails(productID);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();

               /* if(state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(ProductDetailsActivity.this, "You can purchase the more items, Once the order is confirmed", Toast.LENGTH_LONG).show();
                }else {
                    addingToCartList();
                }*/
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM DD, YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH: mm: ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("Pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity_Ordered", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhoneNumber())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhoneNumber())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProductDetailsActivity.this, "added to cart list", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this, Home.class);
                                                startActivity(intent);


                                            }

                                        }
                                    });

                        }

                    }
                });
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    numberButton.setRange(1, Integer.parseInt(products.getQuantity()));
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void CheckOrderState(){
        DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhoneNumber());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String DeliveryState=dataSnapshot.child("state").getValue().toString();
                    String Username=dataSnapshot.child("name").getValue().toString();

                    if(DeliveryState.equals("delivered")){
                       state="Order Shipped";

                    }else if(DeliveryState.equals("not delivered")){

                        state="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
