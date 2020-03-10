package com.example.foodz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView kenyanV, ChineseV, indianV;
    private ImageView drinksV, alcoholV, coffeeV;
    private ImageView chickenV, vegetablesV, saladV;
    private Button CheckOrdersButton, AdminLogout, MaintainBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        kenyanV = findViewById(R.id.kenyan);
        ChineseV = findViewById(R.id.chinese);
        indianV = findViewById(R.id.indian);
        drinksV = findViewById(R.id.drinks);
        alcoholV = findViewById(R.id.alcohol);
        coffeeV = findViewById(R.id.coffee);
        chickenV = findViewById(R.id.chicken);
        vegetablesV = findViewById(R.id.vegetable);
        saladV = findViewById(R.id.salad);
        CheckOrdersButton = findViewById(R.id.adminCheckOrders);
        AdminLogout = findViewById(R.id.adminLogout);
        MaintainBtn=findViewById(R.id.maintain);

        MaintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintain = new Intent(AdminCategoryActivity.this, Home.class);
                maintain.putExtra("Admins", "Admins");
                startActivity(maintain);
            }
        });

        AdminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(i);

            }
        });

        kenyanV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "kenyanV");
                startActivity(intent);
            }
        });

        ChineseV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "ChineseV");
                startActivity(intent);
            }
        });
        indianV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "indianV");
                startActivity(intent);
            }
        });
        drinksV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "drinksV");
                startActivity(intent);
            }
        });
        alcoholV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "alcoholV");
                startActivity(intent);
            }
        });
        coffeeV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "coffeeV");
                startActivity(intent);
            }
        });
        chickenV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "chickenV");
                startActivity(intent);
            }
        });
        vegetablesV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "vegetablesV");
                startActivity(intent);
            }
        });

        saladV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", " saladV");
                startActivity(intent);
            }
        });

    }
}
