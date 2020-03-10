package com.example.foodz;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodz.Adapters.IntroAdapter;
import com.example.foodz.Prevalent.Prevalent;
import com.example.foodz.model.IntroItems;
import com.example.foodz.model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class WelcomeScreen extends AppCompatActivity {
     ViewPager viewPager;
     IntroAdapter introAdapter;
     TabLayout tabIndicator;
     Button  btnGetStarted;
     Animation btnAnim;
     TextView signUp;
     TextView do_not_have;
     RelativeLayout relativeLayout;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        getSupportActionBar().hide();
        viewPager=findViewById(R.id.slideviewPager);
        tabIndicator=findViewById(R.id.indicator);
        btnGetStarted=findViewById(R.id.btnGetStarted);
        btnAnim= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        signUp=findViewById(R.id.signUp);
        do_not_have=findViewById(R.id.doNot);
        relativeLayout=findViewById(R.id.intro_layout);
        loadingBar=new ProgressDialog(this);
        Paper.init(this);

        List<IntroItems> mlist=new ArrayList<>();
        mlist.add(new IntroItems("Fresh Food", "Experience Natural Food with its Original taste", R.drawable.img1, R.drawable.logo));
        mlist.add(new IntroItems("Easy Payment", "Positively Different", R.drawable.img3, R.drawable.logo));
        mlist.add(new IntroItems("Fast Delivery", "Always On time", R.drawable.img2, R.drawable.logo));
        introAdapter=new IntroAdapter(this, mlist);
        tabIndicator.setupWithViewPager(viewPager);
        viewPager.setAdapter(introAdapter);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new myTimerTask(),3000, 4000);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        loadLastScreen();
        String UserPhoneKey=Paper.book().read(Prevalent.UserPhonekey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordkey);
        if(UserPhoneKey !=null && UserPasswordKey!=null){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey, UserPasswordKey);
                loadingBar.setMessage("Already logged in, please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                if(!isConnected()){
                    Toast.makeText(WelcomeScreen.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    loadingBar.show();
                }
            }
        }


    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();


    }

    private void AllowAccess(final String phoneNumber, final  String pwd) {
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(phoneNumber).exists()){

                    Users usersData=dataSnapshot.child("Users").child(phoneNumber).getValue(Users.class);

                    if (usersData.getPhoneNumber().equals(phoneNumber)){

                        if (usersData.getPwd().equals(pwd)){
                            Toast.makeText(WelcomeScreen.this,"You are already logged in", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent home=new Intent(WelcomeScreen.this, Home.class);
                            Prevalent.CurrentOnlineUser=usersData;
                            startActivity(home);
                        }
                        else  {
                            Toast.makeText(WelcomeScreen.this,"Password Incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }else {
                    Toast.makeText(WelcomeScreen.this,"Account "+phoneNumber+"does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadLastScreen() {
        btnGetStarted.setVisibility(View.VISIBLE);
        btnGetStarted.setAnimation(btnAnim);
    }

    public class myTimerTask extends TimerTask{

        @Override
        public void run() {
            WelcomeScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(+1);
                    }else if(viewPager.getCurrentItem()==1){
                        viewPager.setCurrentItem(2);
                    }else viewPager.setCurrentItem(0);
                }
            });
        }
    }

}



