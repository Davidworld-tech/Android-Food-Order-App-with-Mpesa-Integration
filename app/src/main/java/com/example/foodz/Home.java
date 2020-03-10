package com.example.foodz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.foodz.Prevalent.Prevalent;
import com.example.foodz.model.Admins;
import com.example.foodz.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static com.example.foodz.R.*;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Toast backtoast;
    private DatabaseReference productsRef;
    private String type = "";
    private ImageView Cart;
    private SearchView mySearchView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home);
        Paper.init(this);

        Toolbar toolbar = findViewById(id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("Admins").toString();
        }
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        DrawerLayout drawer = findViewById(id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mySearchView=findViewById(id.search);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(id.user_profile_name);


        if (!type.equals("Admins")) {
            userNameTextView.setText(Prevalent.CurrentOnlineUser.getName());
            CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);
            Picasso.get().load(Prevalent.CurrentOnlineUser.getImage()).placeholder(drawable.profile).into(profileImageView);
        }

        recyclerView = findViewById(id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, productsAdapter> adapter = new FirebaseRecyclerAdapter<Products, productsAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull productsAdapter holder, int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtDescription.setText(model.getDescription());
                holder.txtPrice.setText("Price " + model.getPrice() + " Ksh");
                holder.txtQuantity.setText(model.getQuantity() + " Items Left");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (type.equals("Admins")) {
                            Intent det = new Intent(Home.this, AdminMaintainActivity.class);
                            det.putExtra("pid", model.getPid());
                            startActivity(det);

                        } else {
                            Intent det = new Intent(Home.this, ProductDetailsActivity.class);
                            det.putExtra("pid", model.getPid());
                            startActivity(det);
                        }


                    }
                });
            }


            @NonNull
            @Override
            public productsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                productsAdapter holder = new productsAdapter(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        switch (item.getItemId()) {
            case id.cart:
                if (!type.equals("Admins")) {
                    Intent c = new Intent(Home.this, CartActivity.class);
                    startActivity(c);
                }
                break;
            case id.logout:
                if (!type.equals("Admins")) {
                    Intent c = new Intent(Home.this, Login.class);
                    startActivity(c);
                    finish();
                }
                break;

            case id.search:
                if (!type.equals("Admins")) {
                    mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            return false;
                        }
                    });

                }
                break;

        }

        return true;

    }




    @Override
    public void onBackPressed() {
        if (backtoast != null && backtoast.getView().getWindowToken() != null) {
            finish();
        } else {
            backtoast = Toast.makeText(this, "Press back again to logout", Toast.LENGTH_SHORT);
            backtoast.show();
        }

    }



    @Override

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case id.nav_cart:
                if (!type.equals("Admins")) {
                    Intent c = new Intent(Home.this, CartActivity.class);
                    startActivity(c);
                }
                break;

            case id.nav_orders:
                if (!type.equals("Admins")) {

                    Toast.makeText(this, "On development", Toast.LENGTH_SHORT).show();
                }
                break;
            case id.nav_categories:
                if (!type.equals("Admins")) {
                    Toast.makeText(this, "Not yet developed", Toast.LENGTH_SHORT).show();
                }
                break;
            case id.nav_settings:
                if (!type.equals("Admins")) {
                    Intent set = new Intent(Home.this, Settings.class);
                    startActivity(set);
                }
                break;
            case id.nav_logout:

                if (!type.equals("Admins")) {
                    Paper.book().destroy();
                    Intent intent = new Intent(Home.this, WelcomeScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }


        }
        menuItem.setChecked(true);
        DrawerLayout drawer = findViewById(id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
