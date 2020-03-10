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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodz.Adapters.CartViewHolder;
import com.example.foodz.Prevalent.Prevalent;
import com.example.foodz.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button complete_order_btn;
    private TextView TotalAmount, txtMsgOne;
    private int overTotalPrice=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //hideActionBar
        getSupportActionBar().hide();

        recyclerView=findViewById(R.id.cartList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        complete_order_btn=findViewById(R.id.complete_button);
        TotalAmount=findViewById(R.id.totalPrice);
        txtMsgOne=findViewById(R.id.msgOne);

        complete_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this, ConfirmFinalOrder.class);
                intent.putExtra("Total Amount", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
       // CheckOrderState();
        final DatabaseReference cartListReference= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListReference.child("User View")
                .child(Prevalent.CurrentOnlineUser.getPhoneNumber())
                .child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText( "Quantity= " +model.getQuantity_Ordered());
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("@  "+model.getPrice()+" Ksh");
                holder.txtTotal.setText("Total=  "+(Integer.valueOf(model.getPrice())* Integer.valueOf(model.getQuantity_Ordered())));

                final int oneTypeProductTotalPrice=((Integer.valueOf(model.getPrice())))* Integer.valueOf(model.getQuantity_Ordered());
                overTotalPrice=overTotalPrice+oneTypeProductTotalPrice;
                TotalAmount.setText("Total Amount = Ksh "+overTotalPrice);

               model.getQuantity();
               model.getQuantity_Ordered();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{

                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i==0){
                                    Intent intent=new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                }if(i==1){
                                    cartListReference.child("User View")
                                            .child(Prevalent.CurrentOnlineUser.getPhoneNumber())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this,"Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                        TotalAmount.setText("Total Amount = Ksh "+(overTotalPrice=overTotalPrice-oneTypeProductTotalPrice));
                                                    }
                                                }
                                            });
                                }

                            }
                        });
                        builder.show();

                    }
                });

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

    private void CheckOrderState(){
        DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhoneNumber());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String DeliveryState=dataSnapshot.child("state").getValue().toString();
                    String Username=dataSnapshot.child("name").getValue().toString();

                    if(DeliveryState.equals("delivered")){
                        TotalAmount.setText("Dear "+ Username+ "\n  Your order was delivered successfully");
                        recyclerView.setVisibility(View.GONE);
                        txtMsgOne.setVisibility(View.VISIBLE);
                        txtMsgOne.setText("Congraturation your final order has been received successfully, and its waiting for verification");
                        complete_order_btn.setVisibility(View.GONE);

                    }else if(DeliveryState.equals("not delivered")){
                        TotalAmount.setText("Not delivered");
                        recyclerView.setVisibility(View.GONE);
                        txtMsgOne.setVisibility(View.VISIBLE);
                        complete_order_btn.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
