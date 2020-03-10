package com.example.foodz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SearchProductsActivity extends AppCompatActivity {
    private Button searchButton;
    private EditText searchInput;
    private RecyclerView searchRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        searchInput=findViewById(R.id.searchProducts);
        searchButton=findViewById(R.id.searchBtn);
        searchRecView=findViewById(R.id.searchView);

        searchRecView.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
    }
}
