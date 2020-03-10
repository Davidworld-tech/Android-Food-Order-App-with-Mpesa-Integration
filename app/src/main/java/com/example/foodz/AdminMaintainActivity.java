package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodz.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainActivity extends AppCompatActivity {
    private Button applyChangesButton, Delete_button;
    private EditText name, price, quantity, description;
    private ImageView imageMaintain;
    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain);
        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChangesButton = findViewById(R.id.product_maintain_btn);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        quantity = findViewById(R.id.available_display_maintain);
        description = findViewById(R.id.product_description_maintain);
        imageMaintain = findViewById(R.id.product_image_maintain);
        Delete_button=findViewById(R.id.Delete);
        Delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });
        displaySpecificProductInfo();

        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });
    }

    private void deleteThisProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent=new Intent(AdminMaintainActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String Pname=name.getText().toString();
        String Pprice=price.getText().toString();
        String Pquantity=quantity.getText().toString();
        String Pdescription=description.getText().toString();

        if (Pname.equals("")){
            Toast.makeText(AdminMaintainActivity.this, "Write down product's name", Toast.LENGTH_SHORT).show();
        }else if (Pprice.equals("")){
            Toast.makeText(AdminMaintainActivity.this, "Write down product's price", Toast.LENGTH_SHORT).show();
        }else if (Pquantity.equals("")){
            Toast.makeText(AdminMaintainActivity.this, "Write down product's quantity", Toast.LENGTH_SHORT).show();
        }else if (Pdescription.equals("")){
            Toast.makeText(AdminMaintainActivity.this, "Write down product's description", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String, Object> productMap=new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", Pdescription);
            productMap.put("Pname", Pname);
            productMap.put("quantity", Pquantity);
            productMap.put("price", Pprice );

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminMaintainActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }



    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String Pname = dataSnapshot.child("Pname").getValue().toString();
                    String Pdescription = dataSnapshot.child("description").getValue().toString();
                    String Pprice = dataSnapshot.child("price").getValue().toString();
                    String Pquantity = dataSnapshot.child("quantity").getValue().toString();
                    String Pimage = dataSnapshot.child("image").getValue().toString();
                    name.setText(Pname);
                    description.setText(Pdescription);
                    price.setText(Pprice);
                    quantity.setText(Pquantity);
                    Picasso.get().load(Pimage).into(imageMaintain);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
