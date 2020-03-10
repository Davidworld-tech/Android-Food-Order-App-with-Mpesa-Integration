package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodz.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Settings extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText, addressEditText;
    private TextView profileChangeTextBtn, closeTextBtn, updateTextButton;
    private Uri imageUri;
    private String myUrl="";
    private StorageReference storageProfilePictureRef;
    private String checker="";
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImageView=findViewById(R.id.setting_profile_image);
        fullNameEditText=findViewById(R.id.setting_fullName);
        userPhoneEditText=findViewById(R.id.setting_phone_number);
        addressEditText=findViewById(R.id.setting_address);
        profileChangeTextBtn=findViewById(R.id.profile_image_change_btn);
        closeTextBtn=findViewById(R.id.close_setting);
        updateTextButton=findViewById(R.id.update_account_setting);
        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        UserInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }else {
                    updateOnlyUserInfo();
                }
            }
        });


        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .start( Settings.this);


            }
        });

    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> UserMap=new HashMap<>();
        UserMap.put("name" , fullNameEditText.getText().toString());
        UserMap.put("address" , addressEditText.getText().toString());
        UserMap.put("phoneNumber" , userPhoneEditText.getText().toString());
        ref.child(Prevalent.CurrentOnlineUser.getPhoneNumber()).updateChildren(UserMap);


        startActivity(new Intent(Settings.this, Home.class));
        Toast.makeText(Settings.this, "Profile update successful", Toast.LENGTH_SHORT).show();
        finish();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
        }else {
            Toast.makeText(Settings.this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this, Settings.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(fullNameEditText.getText().toString())){
            Toast.makeText(Settings.this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(Settings.this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userPhoneEditText.getText().toString())){
            Toast.makeText(Settings.this, "Phone Number is mandatory", Toast.LENGTH_SHORT).show();
        }else if(checker.equals("clicked")){
            upLoadImage();
        }
    }

    private void upLoadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!= null){
            final StorageReference fileRef= storageProfilePictureRef
                    .child(Prevalent.CurrentOnlineUser.getPhoneNumber());
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl =task.getResult();
                        myUrl=downloadUrl.toString();
                        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> UserMap=new HashMap<>();
                        UserMap.put("name" , fullNameEditText.getText().toString());
                        UserMap.put("address" , addressEditText.getText().toString());
                        UserMap.put("phoneNumber" , userPhoneEditText.getText().toString());
                        UserMap.put("image" , myUrl);
                        ref.child(Prevalent.CurrentOnlineUser.getPhoneNumber()).updateChildren(UserMap);
                        progressDialog.dismiss();

                        startActivity(new Intent(Settings.this, Home.class));
                        Toast.makeText(Settings.this, "Profile update successful", Toast.LENGTH_SHORT).show();
                        finish();

                    }else {
                        Toast.makeText(Settings.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }else {
            Toast.makeText(Settings.this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void UserInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText) {
        DatabaseReference UsersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhoneNumber());

        UsersRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image=dataSnapshot.child("image").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();
                        String phoneNumber=dataSnapshot.child("phoneNumber").getValue().toString();
                        String address=dataSnapshot.child("address").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phoneNumber);
                        addressEditText.setText(address);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
