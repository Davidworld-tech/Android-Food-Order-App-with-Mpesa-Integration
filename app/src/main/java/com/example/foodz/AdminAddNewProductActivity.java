package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.UpdateLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodz.Prevalent.Prevalent;
import com.example.foodz.model.Admins;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName, description, price, Pname,  quantity, saveCurrentDate, saveCurrentTime, productRandomKey, downloadImageUrl;
    private ImageView inputproductImage;
    private Button addProductButton;
    private EditText inputProductName, inputProductDescription, inputProductPrice, inputProductQuantity;
    private static final int GalleryPic=1;
    private Uri imageUri;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductsRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        inputproductImage=findViewById(R.id.select_product_image);
        addProductButton=findViewById(R.id.new_product);
        inputProductName=findViewById(R.id.product_name);
        inputProductDescription=findViewById(R.id.productDescription);
        inputProductPrice=findViewById(R.id.product_price);
        inputProductQuantity=findViewById(R.id.Items_Available);
        loadingBar=new ProgressDialog(this);
        categoryName=getIntent().getExtras().get("category").toString();
        ProductImageRef= FirebaseStorage.getInstance().getReference().child("product images");
        ProductsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        inputproductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

    }


    private void openGallery() {

        Intent galleryIntent =new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPic );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode==GalleryPic && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            inputproductImage.setImageURI(imageUri);
        }
    }


    private void validateProductData() {
     description=inputProductDescription.getText().toString();
     price=inputProductPrice.getText().toString();
     Pname=inputProductName.getText().toString();
     quantity=inputProductQuantity.getText().toString();

     if(imageUri==null){
         Toast.makeText(AdminAddNewProductActivity.this,"Product image is required", Toast.LENGTH_SHORT).show();
     }else if(TextUtils.isEmpty(description)){
         Toast.makeText(AdminAddNewProductActivity.this,"please write product description", Toast.LENGTH_SHORT).show();
     }else if(TextUtils.isEmpty(price)){
         Toast.makeText(AdminAddNewProductActivity.this,"product price is required", Toast.LENGTH_SHORT).show();
     }else if(TextUtils.isEmpty(Pname)){
         Toast.makeText(AdminAddNewProductActivity.this,"please write product name", Toast.LENGTH_SHORT).show();
     }else if(TextUtils.isEmpty(quantity)){
         Toast.makeText(AdminAddNewProductActivity.this,"please add available items quantity", Toast.LENGTH_SHORT).show();
     } else {
         storeProductInformation();
     }

    }

    private void storeProductInformation() {
        loadingBar.setMessage("Adding new product, please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        if(!isConnected()){
            Toast.makeText(AdminAddNewProductActivity.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.show();
        }
        Calendar calendar = Calendar.getInstance();
        saveCurrentDate= DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat(", HH: mm: ss a ");

        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTime;

        final StorageReference filePath=ProductImageRef.child(imageUri.getLastPathSegment()+ productRandomKey);

        final UploadTask uploadTask=filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message= e.toString();
                Toast.makeText(AdminAddNewProductActivity.this,"error "+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Product image uploaded successfully ", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,"getting image product successful ", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }

                    }
                });
            }
        });
    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void saveProductInfoToDatabase() {


        HashMap<String, Object> productMap=new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("price", price );
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("Pname", Pname);
        productMap.put("quantity", quantity);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Intent intent=new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                               startActivity(intent);
                               loadingBar.dismiss();
                               Toast.makeText(AdminAddNewProductActivity.this,"product is added successfully ", Toast.LENGTH_SHORT).show();
                           }else {
                               loadingBar.dismiss();
                               String message=task.getException().toString();
                               Toast.makeText(AdminAddNewProductActivity.this,"error "+message, Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
    }
}
