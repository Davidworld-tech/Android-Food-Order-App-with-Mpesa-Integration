package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodz.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrder extends AppCompatActivity {
    private EditText NameEditText, PhoneEditText, AddressEditTxt;
    private Button ConfirmButton;
    private String totalAmount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        NameEditText = findViewById(R.id.delivery_name);
        PhoneEditText = findViewById(R.id.delivery_phoneNumber);
        AddressEditTxt = findViewById(R.id.deliveryAddress);
        ConfirmButton = findViewById(R.id.confirm);
        totalAmount = getIntent().getStringExtra("Total Amount");
        Toast.makeText(this, "Total Amount =: " + totalAmount, Toast.LENGTH_SHORT).show();


        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    private void Check() {

        if (TextUtils.isEmpty(NameEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide Your Full Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(PhoneEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide Your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(AddressEditTxt.getText().toString())) {
            Toast.makeText(this, "Please Provide Your Address", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        saveCurrentDate = DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat(", HH: mm: ss a ");

        saveCurrentTime = currentTime.format(calendar.getTime());


        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.CurrentOnlineUser.getPhoneNumber());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", NameEditText.getText().toString());
        ordersMap.put("phoneNumber", PhoneEditText.getText().toString());
        ordersMap.put("Address", AddressEditTxt.getText().toString());
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("state", "not delivered");
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                            .child(Prevalent.CurrentOnlineUser.getPhoneNumber())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ConfirmFinalOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmFinalOrder.this, MpesaPayment.class);
                                intent.putExtra("Total Amount", String.valueOf(totalAmount));
                                startActivity(intent);
                                finish();

                            }

                        }
                    });

                }

            }
        });

    }


}
