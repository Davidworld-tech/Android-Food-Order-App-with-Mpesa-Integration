package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodz.Prevalent.Prevalent;

import com.example.foodz.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    TextView Reg;
    EditText phone, loginPassword;
    Button loginButton;
    ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private CheckBox checkBoxRememberMe;
    private TextView adminLink;
    private TextView notAdminLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        phone = findViewById(R.id.l_phoneNumber);
        loginPassword = findViewById(R.id.l_password);
        Reg = findViewById(R.id.redirect_signUp);
        loginButton = findViewById(R.id.btnLogin);
        adminLink = findViewById(R.id.adminLoginPanel);
        notAdminLink = findViewById(R.id.notAdmin);
        loadingBar = new ProgressDialog(this);
        checkBoxRememberMe = findViewById(R.id.remember_me_check);
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });
        Paper.init(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("LOGIN ADMIN");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("LOGIN");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();


    }

    private void loginUser() {

        String phoneNumber = phone.getText().toString();
        String pwd = loginPassword.getText().toString();

        if (pwd.isEmpty()) {
            loginPassword.setError("Please enter password");
            loginPassword.requestFocus();
        } else if (phoneNumber.isEmpty()) {
            phone.setError("Please enter your phone number");
            phone.requestFocus();
        }else if(phoneNumber.length()<10 ) {
           phone.setError("minimum 10 numbers");
            phone.requestFocus();
        } else {
            loadingBar.setMessage("Authenticating, please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            if (!isConnected()) {
                Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.show();
            }


            allowAccess(phoneNumber, pwd);
        }
    }

    private void allowAccess(final String phoneNumber, final String pwd) {
        if (checkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhonekey, phoneNumber);
            Paper.book().write(Prevalent.UserPasswordkey, pwd);
        }
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(phoneNumber).exists()) {

                    Users usersData = dataSnapshot.child(parentDbName).child(phoneNumber).getValue(Users.class);

                    if (usersData.getPhoneNumber().equals(phoneNumber)) {

                        if (usersData.getPwd().equals(pwd)) {
                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent home = new Intent(Login.this, AdminCategoryActivity.class);
                                Prevalent.CurrentOnlineUser = usersData;
                                startActivity(home);
                            } else if (parentDbName.equals("Users")) {
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent home = new Intent(Login.this, Home.class);
                                Prevalent.CurrentOnlineUser = usersData;
                                startActivity(home);
                            }
                        } else {
                            Toast.makeText(Login.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                } else {
                    Toast.makeText(Login.this, "Account " + phoneNumber + " does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingBar.dismiss();
            }
        });
    }

}
