package com.example.foodz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MpesaPayment extends AppCompatActivity {

    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.sendButton)
    Button sendButton;
    private String totalPayable = "";

    //Declare Daraja :: Global Variable
    Daraja daraja;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);
        ButterKnife.bind(this);

        totalPayable = getIntent().getStringExtra("Total Amount");
        getSupportActionBar().hide();

        ButterKnife.bind(this);


        //Init Daraja
        //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
        daraja = Daraja.with("dIzGerG5mEhWWb7eh0FWJ5Ox2vb4ybFV", "Q0DRYxtMFqFfc0R0", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(MpesaPayment.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(MpesaPayment.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(MpesaPayment.this.getClass().getSimpleName(), error);
            }
        });

        sendButton.setOnClickListener(v -> {

            //Get Phone Number from User Input
            phoneNumber = editTextPhoneNumber.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber)) {
                editTextPhoneNumber.setError("Please Provide a Phone Number");
                return;
            }
           LNMExpress lnmExpress=new LNMExpress(

                   "174379",
                   "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                   TransactionType.CustomerBuyGoodsOnline, // TransactionType.CustomerPayBillOnline  <- Apply any of these two
                   totalPayable,
                   "254708374149",
                   "174379",
                   phoneNumber,
                   "http://mycallbackurl.com/checkout.php",
                   "001ABC",
                   "Goods Payment"
           );

            //This is the
            daraja.requestMPESAExpress(lnmExpress,
                    new DarajaListener<LNMResult>() {
                        @Override
                        public void onResult(@NonNull LNMResult lnmResult) {
                            Log.i(MpesaPayment.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i(MpesaPayment.this.getClass().getSimpleName(), error);
                        }
                    }
            );
        });
    }
}
