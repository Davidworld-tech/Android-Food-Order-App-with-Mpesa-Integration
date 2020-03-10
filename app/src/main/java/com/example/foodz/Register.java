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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    TextView login_register, need_help, password, E_mail, phone_number , usernane;
    EditText registerUsername, registerPhoneNumber, registerEmail, registerPassword;
    FirebaseAuth mFirebaseAuth;
    Button register;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();


        mFirebaseAuth= FirebaseAuth.getInstance();
        registerUsername=findViewById(R.id.r_userName);
        registerPhoneNumber=findViewById(R.id.r_phoneNumber);
        registerEmail=findViewById(R.id.r_email);
        registerPassword=findViewById(R.id.r_password);
        register=findViewById(R.id.registerButton);
        loading=new ProgressDialog(this);


        register.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount();
            }});


        login_register=findViewById(R.id.bottom_login);
        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });




    }


    private void createAccount() {
        String name=registerUsername.getText().toString();
        String phoneNumber=registerPhoneNumber.getText().toString();
        String email=registerEmail.getText().toString();
        String pwd=registerPassword.getText().toString();



        if(email.isEmpty()){
            registerEmail.setError("Please enter email id");
            registerEmail.requestFocus();
        }else if(pwd.isEmpty()){
            registerPassword.setError("Please enter password");
            registerPassword.requestFocus();
        }else if(name.isEmpty()){
            registerUsername.setError("Please enter username");
            registerUsername.requestFocus();
        }else if(phoneNumber.isEmpty()){
            registerPhoneNumber.setError("Please enter your phone number");
            registerPhoneNumber.requestFocus();

        }else if(phoneNumber.length()<10 ) {
            registerPhoneNumber.setError("minimum 10 numbers");
            registerPhoneNumber.requestFocus();
        }
        else {
            loading.setTitle("Create account");
            loading.setMessage("Just a moment");
            loading.setCanceledOnTouchOutside(false);
            if(!isConnected()){
                Toast.makeText(Register.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
            }else {
                loading.show();
            }

            validateDetails(name, phoneNumber,email,pwd);
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();


    }

    private void validateDetails(final String name, final String phoneNumber, final String email, final String pwd) {

        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phoneNumber).exists())){
                    HashMap<String,Object> userDataMap=new HashMap<>();
                    userDataMap.put("phoneNumber",phoneNumber);
                    userDataMap.put("name",name);
                    userDataMap.put("email",email);
                    userDataMap.put("pwd", pwd);
                    rootRef.child("Users").child(phoneNumber).updateChildren(userDataMap).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(Register.this,"Congratulations, your account has been created", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                startActivity(new Intent(Register.this, Login.class));
                            }else {
                                Toast.makeText(Register.this,"Sign up unsuccessful, please try again", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                            }
                        }

                    });


                }else {
                    Toast.makeText(Register.this, "This " +phoneNumber+ "already exists", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    Toast.makeText(Register.this, "Please use another number " , Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(Register.this, Login.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
